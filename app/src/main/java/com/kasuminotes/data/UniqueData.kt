package com.kasuminotes.data

import kotlin.math.ceil

data class UniqueData(
    val equipmentId: Int,
    val equipmentName: String,
    val description: String,
    val baseProperty: Property,
    val growthProperty: Property,
    val rfGrowthProperty: Property?
) {
    fun getProperty(uniqueLevel: Int): Property {
        if (uniqueLevel < 1) return Property.zero
        if (uniqueLevel == 1) return baseProperty

        return if (rfGrowthProperty == null || uniqueLevel < 261) {
            Property { index ->
                baseProperty[index] +
                        ceil(growthProperty[index] * (uniqueLevel - 1))
            }
        } else {
            Property { index ->
                baseProperty[index] +
                        ceil(growthProperty[index] * 259) +
                        ceil(rfGrowthProperty[index] * (uniqueLevel - 260))
            }
        }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = Property.getFields() +
                        ",equipment_id," +
                        "equipment_name," +
                        "description"
            }
            return fields!!
        }
    }
}
