package com.kasuminotes.db

import com.kasuminotes.data.EquipCraft
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.EquipInfo
import com.kasuminotes.data.Property
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

fun AppDatabase.getEquipData(equipmentId: Int): EquipData {
    val sql = """SELECT ${EquipData.getFields("ed", "eer")}
FROM equipment_data AS ed
JOIN equipment_enhance_rate AS eer ON ed.equipment_id=eer.equipment_id
WHERE ed.equipment_id=$equipmentId"""

    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            var i = 0

            val baseProperty = Property { _ ->
                it.getDouble(i++)
            }

            val growthProperty = Property { _ ->
                it.getDouble(i++)
            }

            EquipData(
                it.getInt(i++),
                it.getString(i++),
                it.getString(i++),
                it.getString(i++),
                it.getInt(i++),
                it.getInt(i),
                baseProperty,
                growthProperty
            )
        }
    }
}

//suspend fun AppDatabase.getPromotions(unitId: Int): List<UnitPromotion> {
//    val sql = """SELECT ${UnitPromotion.getFields()}
//FROM unit_promotion WHERE unit_id=$unitId ORDER BY promotion_level DESC"""
//
//    val slotsList = useDatabase {
//        rawQuery(sql, null).use {
//            val list = mutableListOf<List<Int>>()
//
//            while (it.moveToNext()) {
//                val slots = mutableListOf<Int>()
//                var i = 0
//
//                while (i < 6) {
//                    slots.add(it.getInt(i++))
//                }
//
//                list.add(slots)
//            }
//
//            list
//        }
//    }
//
//    return withContext(Dispatchers.IO) {
//        slotsList.map { slots ->
//            async {
//                val equips = slots.map { slotId ->
//                    async {
//                        if (slotId != Helper.NullId) getEquipData(slotId) else null
//                    }
//                }.awaitAll()
//
//                UnitPromotion(
//                    equips[0],
//                    equips[1],
//                    equips[2],
//                    equips[3],
//                    equips[4],
//                    equips[5]
//                )
//            }
//        }.awaitAll()
//    }
//}

private fun AppDatabase.isCraft(equipmentId: Int): Boolean {
    val sql = "SELECT craft_flg FROM equipment_data WHERE equipment_id=$equipmentId"
    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            val craftFlg = it.getInt(0)
            craftFlg == 1
        }
    }
}

suspend fun AppDatabase.getEquipCraft(equipmentId: Int): EquipCraft = getEquipCraft(equipmentId, 1, true)

private suspend fun AppDatabase.getEquipCraft(equipmentId: Int, consumeNum: Int, checked: Boolean): EquipCraft {
    return if (checked || isCraft(equipmentId)) {
        val sql = "SELECT ${EquipCraft.getFields()} FROM equipment_craft WHERE equipment_id=$equipmentId"

        val conditions: List<EquipCraft> = useDatabase {
            rawQuery(sql, null).use {
                it.moveToFirst()
                var i = 0
                val list = mutableListOf<EquipCraft>()

                while (i < 20) {
                    val conditionEquipmentId = it.getInt(i++)
                    if (conditionEquipmentId == 0) break

                    val conditionConsumeNum = it.getInt(i++)
                    list.add(
                        EquipCraft(
                            conditionEquipmentId,
                            conditionConsumeNum,
                            null
                        )
                    )
                }

                list
            }
        }

        val conditionList: List<EquipCraft> = withContext(Dispatchers.IO) {
            conditions.map { condition ->
                async { getEquipCraft(condition.equipmentId, condition.consumeSum, false) }
            }.awaitAll()
        }
        EquipCraft(
            equipmentId,
            consumeNum,
            conditionList
        )
    } else {
        EquipCraft(
            equipmentId,
            consumeNum,
            null
        )
    }
}

fun AppDatabase.getUnique2Craft(equipmentId: Int): Int {
    val sql = """SELECT item_id FROM unique_equip_craft_enhance
jOIN unique_equip_consume_group ON consume_group_id=group_id
WHERE equipment_id=$equipmentId"""

    return useDatabase {
        rawQuery(sql, null).use {
            it.moveToFirst()
            it.getInt(0)
        }
    }
}

fun AppDatabase.getEquipmentPairList(): List<Pair<Int, List<EquipInfo>>> {
    val sql = """SELECT ed.equipment_id,eer.description
FROM equipment_data AS ed
JOIN equipment_enhance_rate AS eer ON ed.equipment_id=eer.equipment_id
WHERE ed.equipment_id<110000 OR ed.equipment_id>10000000"""

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<Pair<Int, MutableList<EquipInfo>>>()
            val typePairList = mutableListOf<Pair<Int, String>>()
            val set = mutableSetOf<Int>()

            while (it.moveToNext()) {
                val equipmentId = it.getInt(0)
                val description = it.getString(1)
                val type = Helper.getEquipType(equipmentId)
                val rarity = Helper.getEquipRarity(equipmentId)

                if (!set.contains(type)) {
                    set.add(type)
                    typePairList.add(type to description)
                }

                val equipInfo = EquipInfo(equipmentId, type)

                val item = list.find { pair -> pair.first == rarity }

                if (item == null) {
                    list.add(rarity to mutableListOf(equipInfo))
                } else {
                    item.second.add(equipInfo)
                }

            }

            EquipInfo.typePairList = typePairList.sortedBy { pair -> pair.first }

            list.forEach { pair ->
                pair.second.sortBy { item -> item.type }
            }

            list.sortedByDescending { pair -> pair.first }
        }
    }
}

fun AppDatabase.getEquipMaterialPairList(): List<Pair<Int, List<EquipInfo>>> {
    val sql = "SELECT equipment_id FROM equipment_data WHERE (equipment_id<140000 OR equipment_id>11000000) AND craft_flg=0"

    return useDatabase {
        rawQuery(sql, null).use {
            val list = mutableListOf<Pair<Int, MutableList<EquipInfo>>>()

            while (it.moveToNext()) {
                val equipmentId = it.getInt(0)
                val type = Helper.getEquipType(equipmentId)
                val rarity = Helper.getEquipRarity(equipmentId)

                val equipInfo = EquipInfo(equipmentId, type)

                val item = list.find { pair -> pair.first == rarity }

                if (item == null) {
                    list.add(rarity to mutableListOf(equipInfo))
                } else {
                    item.second.add(equipInfo)
                }

            }

            list.forEach { pair ->
                pair.second.sortBy { item -> item.type }
            }

            list.sortedByDescending { pair -> pair.first }
        }
    }
}
