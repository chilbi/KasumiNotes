package com.kasuminotes.db

import com.kasuminotes.data.ClanBattlePeriod
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
