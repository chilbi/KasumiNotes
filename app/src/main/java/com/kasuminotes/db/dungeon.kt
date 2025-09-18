package com.kasuminotes.db

import android.database.sqlite.SQLiteException
import com.kasuminotes.data.DungeonAreaData

fun AppDatabase.getDungeonAreaDataList(): List<DungeonAreaData> {
    try {
        val sql = if (existsTable("dungeon_area")) {
            """SELECT t.dungeon_area_id,t.dungeon_name,t.floor_num,t.mode,t.pattern,ep.enemy_id,ep.unit_id,ep.name,wgd.wave_group_id
FROM (
SELECT da.dungeon_area_id,da.dungeon_name,dqd.floor_num,0 AS mode,0 AS pattern,dqd.wave_group_id
FROM dungeon_area AS da
JOIN dungeon_quest_data AS dqd
ON da.dungeon_area_id=dqd.dungeon_area_id AND dqd.quest_type=3
UNION
SELECT da.dungeon_area_id,da.dungeon_name,dqd.floor_num,dsb.mode,0 AS pattern,dsb.wave_group_id
FROM dungeon_area AS da
JOIN dungeon_quest_data AS dqd
ON da.dungeon_area_id=dqd.dungeon_area_id AND dqd.quest_type=4
JOIN dungeon_special_battle AS dsb
ON dqd.quest_id=dsb.quest_id
UNION
SELECT da.dungeon_area_id,da.dungeon_name,dqd.floor_num,0 AS mode,pattern,dpb.wave_group_id
FROM dungeon_area AS da
JOIN dungeon_quest_data AS dqd
ON da.dungeon_area_id=dqd.dungeon_area_id AND dqd.quest_type=6
JOIN dungeon_pattern_battle AS dpb
ON dqd.quest_id=dpb.quest_id
) AS t
JOIN wave_group_data AS wgd
ON t.wave_group_id=wgd.wave_group_id
JOIN enemy_parameter AS ep
ON ep.enemy_id IN (wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
ORDER BY t.dungeon_area_id DESC,t.floor_num DESC,t.mode ASC,t.pattern ASC"""
        } else {
            """SELECT dad.dungeon_area_id,dad.dungeon_name,0 AS floor_num,0 AS mode,0 AS pattern,ep.enemy_id,ep.unit_id,ep.name,wgd.wave_group_id
FROM dungeon_area_data AS dad
JOIN wave_group_data AS wgd
ON dad.wave_group_id=wgd.wave_group_id
JOIN enemy_parameter AS ep
ON ep.enemy_id IN (wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
ORDER BY dad.dungeon_area_id DESC"""
        }
        return useDatabase {
            rawQuery(sql, null).use {
                val list = mutableListOf<DungeonAreaData>()
                while (it.moveToNext()) {
                    var i = 0
                    list.add(
                        DungeonAreaData(
                            it.getInt(i++),
                            it.getString(i++),
                            it.getInt(i++),
                            it.getInt(i++),
                            it.getInt(i++),
                            it.getInt(i++),
                            it.getInt(i++),
                            it.getString(i++),
                            it.getInt(i)
                        )
                    )
                }
                list
            }
        }
    } catch (e: SQLiteException) {
        if (e.message?.contains("no such table") == true) {
            return emptyList()
        } else {
            throw e
        }
    }
}

fun AppDatabase.getEnemyTalentWeaknessMap(enemyIdList: List<Int>): Map<Int, List<Int>> {
    return useDatabase {
        val map = mutableMapOf<Int, List<Int>>()
        if (existsTable("enemy_talent_weakness")) {
            enemyIdList.forEach { enemyId ->
                rawQuery("""SELECT talent_1,talent_2,talent_3,talent_4,talent_5
FROM enemy_talent_weakness as etw
LEFT JOIN talent_weakness as tw ON etw.resist_id=tw.resist_id
WHERE enemy_id=$enemyId""", null).use {
                    if (it.moveToFirst()) {
                        val list = mutableListOf<Int>()
                        var i = 0
                        while (i < 5) {
                            list.add(it.getInt(i++))
                        }
                        map[enemyId] = list
                    } else {
                        map[enemyId] = List(5) { 100 }
                    }
                }
            }
        } else {
            enemyIdList.forEach { enemyId ->
                map[enemyId] = List(5) { 100 }
            }
        }
        map
    }
}
