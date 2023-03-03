package com.kasuminotes.db

import com.kasuminotes.data.ClanBattleMapData
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.Property
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getClanBattlePeriodList(): List<ClanBattlePeriod> {
    val sql = "SELECT clan_battle_id,start_time FROM clan_battle_period ORDER BY clan_battle_id DESC"

    return withIOContext {
        val result = use {
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

        result.map {
            async { it.copy(bossUnitIdList =  getBossUnitIdList(it.clanBattleId)) }
        }.awaitAll()
    }
}

suspend fun AppDatabase.getClanBattleMapDataList(clanBattleId: Int): List<ClanBattleMapData> {
    val sql = """SELECT ${ClanBattleMapData.getFields()} FROM clan_battle_2_map_data
WHERE clan_battle_id=$clanBattleId GROUP BY lap_num_from ORDER BY lap_num_from DESC"""

    return withIOContext {
        val result = use {
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

        result.map { mapData ->
            async {
                mapData.enemyDataList = getEnemyDataList(mapData.waveGroupIdList)
                mapData
            }
        }.awaitAll()
    }
}

suspend fun AppDatabase.getMultiEnemyParts(multiParts: List<Int>): List<EnemyData> {
    val sql = """SELECT ${EnemyData.getFields()}
FROM enemy_parameter AS ep
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE ep.enemy_id IN (${multiParts.joinToString(",")})"""

    return safelyUse {
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

private suspend fun AppDatabase.getEnemyDataList(waveGroupIdList: List<Int>): List<EnemyData> {
    val sql = """SELECT ${EnemyData.getFields()}
FROM wave_group_data AS wgd
LEFT JOIN enemy_parameter AS ep ON ep.enemy_id=wgd.enemy_id_1
LEFT JOIN enemy_m_parts AS emp ON emp.enemy_id=ep.enemy_id
LEFT JOIN unit_enemy_data AS ued ON ued.unit_id=ep.unit_id
WHERE wave_group_id IN (${waveGroupIdList.joinToString(",")})"""

    return safelyUse {
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

private suspend fun AppDatabase.getBossUnitIdList(clanBattleId: Int): List<Int> {
    val sql = """SELECT unit_id FROM enemy_parameter WHERE enemy_id IN (
SELECT enemy_id_1 FROM wave_group_data,
(SELECT * FROM clan_battle_2_map_data WHERE clan_battle_id=$clanBattleId AND lap_num_to=-1)
WHERE wave_group_id IN (wave_group_id_1,wave_group_id_2,wave_group_id_3,wave_group_id_4,wave_group_id_5))"""

    return safelyUse {
        rawQuery(sql, null).use {
            val list = mutableListOf<Int>()
            while (it.moveToNext()) {
                list.add(it.getInt(0))
            }
            list
        }
    }
}