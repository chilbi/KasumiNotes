package com.kasuminotes.data

import kotlin.math.ceil
import kotlin.math.min

data class EquipData(
    val equipmentId: Int,
    val equipmentName: String,
    val equipmentType: String,
    val description: String,
    val promotionLevel: Int,
    val craftFlg: Int,
    val baseProperty: Property,
    val growthProperty: Property
) {
    val maxEnhanceLevel: Int =
        if (promotionLevel > 3) 5 else if (promotionLevel < 3) promotionLevel - 1 else 3

    fun getProperty(enhanceLevel: Int): Property {
        if (enhanceLevel < 0) return Property.zero

        val level = min(enhanceLevel, maxEnhanceLevel)

        if (level == 0) return baseProperty

        return Property { index ->
            baseProperty[index] + ceil(growthProperty[index] * level)// TODO 不确定的取整方式
        }
    }

    fun getPropertyValue(index: Int, equipLevel: Int): Double {
        if (equipLevel < 0) return 0.0

        val level = min(equipLevel, maxEnhanceLevel)

        if (level == 0) return baseProperty[index]

        return baseProperty[index] + ceil(growthProperty[index] * level)// TODO 不确定的取整方式
    }

    companion object {
        private var fields: String? = null

        fun getFields(equipmentDataName: String, equipmentEnhanceRateName: String): String {
            if (fields == null) {
                val baseFields = Property.keys.joinToString(",") { key ->
                    "${equipmentDataName}.${key}"
                }
                val growthFields = Property.keys.joinToString(",") { key ->
                    "${equipmentEnhanceRateName}.${key} AS ${key}_growth"
                }
                fields = "$baseFields,$growthFields," +
                        "${equipmentDataName}.equipment_id," +
                        "${equipmentDataName}.equipment_name," +
                        "${equipmentEnhanceRateName}.description AS equipment_type," +
                        "${equipmentDataName}.description," +
                        "${equipmentDataName}.promotion_level," +
                        "${equipmentDataName}.craft_flg"
            }
            return fields!!
        }
    }
}
