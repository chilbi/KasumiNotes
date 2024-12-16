package com.kasuminotes.db

import com.kasuminotes.data.TalentQuestAreaData
import com.kasuminotes.data.QuestWaveGroupEnemy

fun AppDatabase.hasTalentQuest(): Boolean {
    return existsTables(listOf(
        "talent_quest_area_data",
        "talent_quest_data",
        "talent_quest_wave_group_data",
        "talent_quest_enemy_parameter"
    ))
}

fun AppDatabase.getTalentQuestAreaDataList(): List<TalentQuestAreaData> {
    val sql = "SELECT area_id,area_name,talent_id FROM talent_quest_area_data ORDER BY talent_id ASC"

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<TalentQuestAreaData>()
            while (it.moveToNext()) {
                var i = 0
                list.add(TalentQuestAreaData(
                    it.getInt(i++),
                    it.getString(i++),
                    it.getInt(i)
                ))
            }
            list
        }
    }
}

fun AppDatabase.getTalentQuestDataList(areaId: Int): List<QuestWaveGroupEnemy> {
    val sql = """SELECT qd.quest_id,qd.quest_name,wgd.wave_group_id,ep.enemy_id,ep.unit_id,ep.name
FROM talent_quest_data AS qd
JOIN talent_quest_wave_group_data AS wgd
ON wgd.wave_group_id IN (qd.wave_group_id_1,qd.wave_group_id_2,qd.wave_group_id_3)
JOIN talent_quest_enemy_parameter AS ep
ON ep.enemy_id IN (wgd.id,wgd.enemy_id_1,wgd.enemy_id_2,wgd.enemy_id_3,wgd.enemy_id_4,wgd.enemy_id_5)
WHERE qd.area_id=$areaId
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
