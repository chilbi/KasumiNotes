package com.kasuminotes.data

import kotlin.math.ceil

data class UniqueData(
    val equipmentId: Int,
    val equipmentName: String,
    val description: String,
    val baseProperty: Property,
    val growthProperty: Property
) {
    fun getProperty(uniqueLevel: Int): Property {
        if (uniqueLevel < 1) return Property()
        if (uniqueLevel == 1) return baseProperty

        val level = uniqueLevel - 1
        return Property { index ->
            ceil(growthProperty[index] * level) + baseProperty[index]
        }
    }

    companion object {
        private var fields: String? = null

        fun getFields(uniqueEquipmentDataName: String, uniqueEquipmentEnhanceRateName: String): String {
            if (fields == null) {
                val baseFields = Property.keys.joinToString(",") { key ->
                    "${uniqueEquipmentDataName}.${key}"
                }
                val growthFields = Property.keys.joinToString(",") { key ->
                    "${uniqueEquipmentEnhanceRateName}.${key} AS ${key}_growth"
                }
                fields = "${baseFields}," +
                        "${growthFields}," +
                        "${uniqueEquipmentDataName}.equipment_id," +
                        "${uniqueEquipmentDataName}.equipment_name," +
                        "${uniqueEquipmentDataName}.description"
            }
            return fields!!
        }
    }
}
