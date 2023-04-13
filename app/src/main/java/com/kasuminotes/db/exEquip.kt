package com.kasuminotes.db

import com.kasuminotes.data.ExEquipCategory
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.Property
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getUnitExEquipSlots(unitId: Int): List<ExEquipSlot> {
    return withIOContext {
        if (existsTable("unit_ex_equipment_slot")) {
            val sql = """SELECT slot_category_1,slot_category_2,slot_category_3
FROM unit_ex_equipment_slot WHERE unit_id=$unitId"""

            use {
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
        } else {
            emptyList()
        }
    }
}

suspend fun AppDatabase.getExEquipCategory(category: Int): ExEquipCategory {
    val sql = """SELECT category_name,category_base,outline
FROM ex_equipment_category WHERE category=$category"""

    return safelyUse {
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

suspend fun AppDatabase.getEquippableExList(category: Int): List<Int> {
    val sql = """SELECT ex_equipment_id FROM ex_equipment_data
WHERE category=$category ORDER BY rarity DESC"""

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

suspend fun AppDatabase.getExEquipData(exEquipId: Int): ExEquipData {
    val sql = """SELECT ${ExEquipData.getFields()}
FROM ex_equipment_data WHERE ex_equipment_id=$exEquipId"""

    return withIOContext {
        val exEquipData = use {
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
                    null
                )
            }
        }

        val passiveSkillId1 = exEquipData.passiveSkillId1
        val passiveSkillId2 = exEquipData.passiveSkillId2
        if (passiveSkillId1 == 0 && passiveSkillId2 == 0) {
            exEquipData
        } else {
            val skill1And2 = awaitAll(
                async { getSkillData(passiveSkillId1) },
                async { getSkillData(passiveSkillId2) }
            )
            exEquipData.copy(passiveSkill1 = skill1And2[0], passiveSkill2 = skill1And2[1])
        }
    }
}

