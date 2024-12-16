package com.kasuminotes.db

import com.kasuminotes.data.QuestWaveGroupEnemy

fun AppDatabase.hasMirageQuest(): Boolean {
    return existsTables(listOf(
        "mirage_floor_quest_display",
        "mirage_nemesis_quest_display",
        "mirage_floor_quest",
        "mirage_nemesis_quest",
        "abyss_wave_group_data",
        "abyss_enemy_parameter"
    ))
}

fun AppDatabase.getMirageQuestDataList(tableName: String): List<QuestWaveGroupEnemy> {
    val sql = """SELECT qd.quest_id,qdd.quest_name,wgd.wave_group_id,ep.enemy_id,ep.unit_id,ep.name
FROM $tableName AS qd
JOIN ${tableName}_display AS qdd
ON qdd.quest_id=qd.quest_id
JOIN mirage_wave_group_data AS wgd
ON wgd.wave_group_id=qd.wave_group_id
JOIN mirage_enemy_parameter AS ep
ON ep.enemy_id IN (wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
ORDER BY qd.quest_id DESC"""

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
