package com.kasuminotes.db

import com.kasuminotes.data.ExEquip5ExceedLimit
import com.kasuminotes.data.ExEquipCategory
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.ExEquipSubStatus
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

fun AppDatabase.getUnitExEquipSlots(unitId: Int): List<ExEquipSlot> {
    return if (existsTable("unit_ex_equipment_slot")) {
        val sql = """SELECT slot_category_1,slot_category_2,slot_category_3
FROM unit_ex_equipment_slot WHERE unit_id=$unitId"""

        useDatabase {
            rawQuery(sql, null).use {
                val list = mutableListOf<ExEquipSlot>()
                if (it.moveToFirst()) {
                    var i = 0
                    while (i < 3) {
                        list.add(ExEquipSlot(it.getInt(i++), null))
                    }
                }
                list
            }
        }
    } else emptyList()
}

fun AppDatabase.getExEquipCategory(category: Int): ExEquipCategory {
    val sql = """SELECT category_name,category_base,outline
FROM ex_equipment_category WHERE category=$category"""

    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            ExEquipCategory(
                category,
                it.getString(0),
                it.getString(1),
                it.getString(2)
            )
        }
    }
}

fun AppDatabase.getEquippableExList(category: Int): List<Int> {
    val sql = """SELECT ex_equipment_id FROM ex_equipment_data
WHERE category=$category ORDER BY rarity DESC"""

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

suspend fun AppDatabase.getExEquipData(exEquipId: Int): ExEquipData {
    val sql = """SELECT ${ExEquipData.getFields()}
FROM ex_equipment_data WHERE ex_equipment_id=$exEquipId"""

    var exEquipData = useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            val defaultProperty = Property { _ ->
                it.getInt(i++).toDouble()
            }

            val maxProperty = Property { _ ->
                it.getInt(i++).toDouble()
            }

            ExEquipData(
                exEquipId,
                it.getString(i++),
                it.getString(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                defaultProperty,
                maxProperty,
                null,
                null,
                null,
                null
            )
        }
    }

    val subStatusList = getSubStatusList(exEquipData)
    val exceedLimit = getExceedLimit(exEquipData)
    if (exceedLimit != null) {
        exEquipData = exEquipData.copy(exceedLimit = exceedLimit)
    }

    val passiveSkillId1 = exEquipData.passiveSkillId1
    val passiveSkillId2 = exEquipData.passiveSkillId2
    return if (passiveSkillId1 == 0 && passiveSkillId2 == 0) {
        if (subStatusList == null) exEquipData
        else exEquipData.copy(subStatusList = subStatusList)
    } else {
        withContext(Dispatchers.IO) {
            val skill1And2 = awaitAll(
                async { getSkillData(passiveSkillId1) },
                async { getSkillData(passiveSkillId2) }
            )
            exEquipData.copy(
                passiveSkill1 = skill1And2[0],
                passiveSkill2 = skill1And2[1],
                subStatusList = subStatusList
            )
        }
    }
}

private fun AppDatabase.getSubStatusList(exEquipData: ExEquipData): List<ExEquipSubStatus>? {
    return if (
        exEquipData.rarity > 4 &&
        existsTables(listOf("ex_equipment_sub_status", "ex_equipment_sub_status_group"))
    ) {
        val sql = """SELECT ${ExEquipSubStatus.getFields()}
FROM ex_equipment_sub_status_group AS a
JOIN ex_equipment_sub_status AS b ON a.group_id=b.group_id
WHERE a.ex_equipment_id=${exEquipData.exEquipmentId}"""
        useDatabase {
            rawQuery(sql, null).use {
                val list = mutableListOf<ExEquipSubStatus>()
                while (it.moveToNext()) {
                    val values = mutableListOf<Int>()
                    var i = 0
                    while (i < 5) {
                        values.add(it.getInt(i++))
                    }
                    list.add(ExEquipSubStatus(
                        it.getInt(i),
                        values
                    ))
                }
                list
            }
        }
    } else {
        null
    }
}

private suspend fun AppDatabase.getExceedLimit(exEquipData: ExEquipData): ExEquip5ExceedLimit? {
    return if (
        exEquipData.rarity > 4 &&
        existsTables(listOf("ex_equip_5_exceed_limit_group", "ex_equip_5_exceed_limit_status", "ex_equip_5_exceed_limit_skill"))
    ) {
        val sql = """SELECT type_1,value_1,type_2,value_2,type_3,value_3,type_4,value_4,IFNULL(skill_id, 0) AS skill_id
FROM ex_equip_5_exceed_limit_group AS a
LEFT JOIN ex_equip_5_exceed_limit_status AS b ON a.status_group_id=b.status_group_id
LEFT JOIN ex_equip_5_exceed_limit_skill AS c ON a.skill_group_id=c.group_id AND b.rank=c.rank
WHERE a.ex_equipment_id=${exEquipData.exEquipmentId}
ORDER BY b.rank ASC
"""
        val rawList = useDatabase {
             rawQuery(sql, null).use {
                 val list = mutableListOf<RawExEquip5ExceedLimit>()
                 while (it.moveToNext()) {
                     var i = 0
                     val pairs = mutableListOf<Pair<Int, Double>>()
                     for (j in 1..4) {
                         val type = it.getInt(i++)
                         val value = it.getInt(i++).toDouble()
                         if (type > 0) {
                             pairs.add(type to value)
                         }
                     }
                     list.add(RawExEquip5ExceedLimit(
                         it.getInt(i),
                         Property(pairs),
                         null
                     ))
                 }
                 list
             }
        }

        if (rawList.isEmpty()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                val list = rawList.map { raw ->
                    async {
                        raw.skillData = getSkillData(raw.skillId)
                        raw
                    }
                }.awaitAll()
                val skillList = mutableListOf<SkillData?>()
                val statusList = mutableListOf<Property>()
                for (item in list) {
                    skillList.add(item.skillData)
                    statusList.add(item.status)
                }
                ExEquip5ExceedLimit(skillList, statusList)
            }
        }
    } else {
        null
    }
}

private data class RawExEquip5ExceedLimit(
//    val statusGroupId: Int,
//    val skillGroupId: Int,
//    val rank: Int,
    val skillId: Int,
    val status: Property,
    var skillData: SkillData?
)
