package com.kasuminotes.db

import com.kasuminotes.data.TalentQuestAreaData
import com.kasuminotes.data.TalentQuestData

fun AppDatabase.hasTalentQuest(): Boolean {
    return existsTable("talent_quest_area_data") &&
            existsTable("talent_quest_data") &&
            existsTable("talent_quest_wave_group_data") &&
            existsTable("talent_quest_enemy_parameter")
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

fun AppDatabase.getTalentQuestDataList(areaId: Int): List<TalentQuestData> {
    val sql = """SELECT tqd.quest_id,tqd.quest_name,tqwgd.wave_group_id,tqep.enemy_id,tqep.unit_id,tqep.name
FROM talent_quest_data AS tqd
JOIN talent_quest_wave_group_data AS tqwgd ON tqwgd.wave_group_id IN (tqd.wave_group_id_1,tqd.wave_group_id_2,tqd.wave_group_id_3)
JOIN talent_quest_enemy_parameter AS tqep ON tqep.enemy_id IN (tqwgd.id,tqwgd.enemy_id_1,tqwgd.enemy_id_2,tqwgd.enemy_id_3,tqwgd.enemy_id_4,tqwgd.enemy_id_5)
WHERE tqd.area_id=$areaId
ORDER BY tqd.quest_id DESC"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<TalentQuestData>()
            while (it.moveToNext()) {
                var i = 0
                list.add(TalentQuestData(
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
