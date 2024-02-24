package com.kasuminotes.db

import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

suspend fun AppDatabase.getClanBattlePeriodList(limit: Boolean): List<ClanBattlePeriod> {
    var sql = "SELECT clan_battle_id,start_time FROM clan_battle_period ORDER BY clan_battle_id DESC"
    if (limit) {
        sql += " LIMIT 13"
    }

    val result = useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<ClanBattlePeriod>()
            while (it.moveToNext()) {
                list.add(
                    ClanBattlePeriod(
                        it.getInt(0),
                        it.getString(1),
                        emptyList()
                    )
                )
            }
            list
        }
    }

    return withContext(Dispatchers.IO) {
        var list = result.map {
            async { it.copy(bossUnitIdList = getBossUnitIdList(it.clanBattleId)) }
        }.awaitAll()
        if (list.isNotEmpty() && list[0].clanBattleId > 1061) {
            val p62 = list.find { it.clanBattleId == 1062 }
            if (p62 == null || p62.bossUnitIdList.isEmpty()) {
                list.forEach {
                    if (it.clanBattleId > 1062) {
                        it.periodNum -= 1
                    }
                }
            }
        }
        list = list.filter { clanBattlePeriod ->
            clanBattlePeriod.bossUnitIdList.isNotEmpty()
        }
        list
    }
}

suspend fun AppDatabase.getClanBattleMapDataList(clanBattleId: Int): List<ClanBattleMapData> {
    val sql = """SELECT ${ClanBattleMapData.getFields()} FROM clan_battle_2_map_data
WHERE clan_battle_id=$clanBattleId GROUP BY lap_num_from ORDER BY lap_num_from DESC"""

    val result = useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<ClanBattleMapData>()
            while (it.moveToNext()) {
                var i = 0

                val waveGroupIdList = mutableListOf<Int>()
                while (i < 5) {
                    waveGroupIdList.add(it.getInt(i++))
                }

                val scoreCoefficientList = mutableListOf<Float>()
                while (i < 10) {
                    scoreCoefficientList.add(it.getFloat(i++))
                }

                list.add(
                    ClanBattleMapData(
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i),
                        scoreCoefficientList,
                        waveGroupIdList
                    )
                )
            }
            list
        }
    }

    return withContext(Dispatchers.IO) {
        result.map { mapData ->
            async {
                mapData.enemyDataList = getEnemyDataList(mapData.waveGroupIdList)
                mapData
            }
        }.awaitAll()
    }
}

fun AppDatabase.getMultiEnemyParts(multiParts: List<Int>): List<EnemyData> {
    val sql = """SELECT ${EnemyData.getFields()}
FROM enemy_parameter AS ep
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE ep.enemy_id IN (${multiParts.joinToString(",")})"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<EnemyData>()
            while (it.moveToNext()) {
                var i = 0

                val mainSkillLvList = mutableListOf<Int>()
                while (i < 10) {
                    mainSkillLvList.add(it.getInt(i++))
                }

                val exSkillLvList = mutableListOf<Int>()
                while (i < 15) {
                    exSkillLvList.add(it.getInt(i++))
                }

                val parts = emptyList<Int>()
                i = 20
//                while (i < 20) {
//                    val id = it.getInt(i++)
//                    if (id != 0) {
//                        parts.add(id)
//                    }
//                }

                val property = Property { _ ->
                    it.getDouble(i++)
                }

                list.add(
                    EnemyData(
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getFloat(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i),
                        mainSkillLvList,
                        exSkillLvList,
                        parts,
                        property
                    )
                )
            }
            list
        }
    }
}

private fun AppDatabase.getEnemyDataList(waveGroupIdList: List<Int>): List<EnemyData> {
    val sql = """SELECT ${EnemyData.getFields()}
FROM wave_group_data AS wgd
LEFT JOIN enemy_parameter AS ep ON ep.enemy_id=wgd.enemy_id_1
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE wave_group_id IN (${waveGroupIdList.joinToString(",")})"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<EnemyData>()
            while (it.moveToNext()) {
                var i = 0

                val mainSkillLvList = mutableListOf<Int>()
                while (i < 10) {
                    mainSkillLvList.add(it.getInt(i++))
                }

                val exSkillLvList = mutableListOf<Int>()
                while (i < 15) {
                    exSkillLvList.add(it.getInt(i++))
                }

                val multiParts = mutableListOf<Int>()
                while (i < 20) {
                    val id = it.getInt(i++)
                    if (id != 0) {
                        multiParts.add(id)
                    }
                }

                val property = Property { _ ->
                    it.getDouble(i++)
                }

                list.add(
                    EnemyData(
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getFloat(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i),
                        mainSkillLvList,
                        exSkillLvList,
                        multiParts,
                        property
                    )
                )
            }
            list
        }
    }
}

private fun AppDatabase.getBossUnitIdList(clanBattleId: Int): List<Int> {
    val sql = """SELECT unit_id FROM enemy_parameter WHERE enemy_id IN (
SELECT enemy_id_1 FROM wave_group_data,
(SELECT * FROM clan_battle_2_map_data WHERE clan_battle_id=$clanBattleId AND lap_num_to=-1)
WHERE wave_group_id IN (wave_group_id_1,wave_group_id_2,wave_group_id_3,wave_group_id_4,wave_group_id_5))"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<Int>()
            while (it.moveToNext()) {
                list.add(it.getInt(0))
            }
            list
        }
    }
}
