package com.kasuminotes.db

import com.kasuminotes.data.ExEquipSlot

suspend fun AppDatabase.getUnitExEquipSlots(unitId: Int): List<ExEquipSlot> {
    return withIOContext {
        if (existsTable("unit_ex_equipment_slot")) {
            val sql = """SELECT slot_category_1,slot_category_2,slot_category_3
FROM unit_ex_equipment_slot WHERE unit_id=$unitId"""

            use {
                rawQuery(sql, null).use {
                    it.moveToFirst()
                    val list = mutableListOf<ExEquipSlot>()
                    var i = 0
                    while (i < 3) {
                        list.add(ExEquipSlot(it.getInt(i++), null))
                    }
                    list
                }
            }
        } else {
            emptyList()
        }
    }

}
