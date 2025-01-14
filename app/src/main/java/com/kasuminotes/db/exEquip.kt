package com.kasuminotes.db

import com.kasuminotes.data.ExEquipCategory
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.ExEquipSubStatus
import com.kasuminotes.data.Property
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

    val exEquipData = useDatabase {
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
                null
            )
        }
    }

    val subStatusList = getSubStatusList(exEquipData)

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
