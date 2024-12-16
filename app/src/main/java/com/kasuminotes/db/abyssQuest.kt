package com.kasuminotes.db

import com.kasuminotes.data.AbyssSchedule
import com.kasuminotes.data.QuestWaveGroupEnemy

fun AppDatabase.hasAbyssQuest(): Boolean {
    return existsTables(listOf(
        "abyss_schedule",
        "abyss_quest_data",
        "abyss_boss_data",
        "abyss_wave_group_data",
        "abyss_enemy_parameter"
    ))
}

fun AppDatabase.getAbyssScheduleList(): List<AbyssSchedule> {
    val sql = """SELECT abyss_id,talent_id,title,start_time,end_time
FROM abyss_schedule ORDER BY abyss_id DESC
"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<AbyssSchedule>()
            while (it.moveToNext()) {
                var i = 0
                list.add(AbyssSchedule(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getString(i++),
                    it.getString(i++),
                    it.getString(i)
                ))
            }
            list
        }
    }
}

fun AppDatabase.getAbyssQuestDataList(abyssId: Int): List<QuestWaveGroupEnemy> {
    val sql = """SELECT qd.quest_id,qd.quest_name,wgd.wave_group_id,ep.enemy_id,ep.unit_id,ep.name
FROM abyss_quest_data AS qd
JOIN abyss_wave_group_data AS wgd
ON wgd.wave_group_id=qd.wave_group_id
JOIN abyss_enemy_parameter AS ep
ON ep.enemy_id IN (wgd.id,wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
WHERE qd.abyss_id=$abyssId
UNION
SELECT 0 AS quest_id,"BOSS" AS quest_name,wgd.wave_group_id,ep.enemy_id,ep.unit_id,ep.name
FROM abyss_boss_data AS bd
JOIN abyss_wave_group_data AS wgd
ON wgd.wave_group_id=bd.wave_group_id
JOIN abyss_enemy_parameter AS ep
ON ep.enemy_id IN (wgd.id,wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
WHERE bd.abyss_id=$abyssId"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<QuestWaveGroupEnemy>()
            while (it.moveToNext()) {
                var i = 0
                list.add(QuestWaveGroupEnemy(
                    it.getInt(i++),
                    it.getString(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getString(i)
                ))
            }
            list
        }
    }
}
