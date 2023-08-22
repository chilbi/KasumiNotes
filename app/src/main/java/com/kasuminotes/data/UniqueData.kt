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
    fun getUnique1Property(uniqueLevel: Int): Property {
        if (uniqueLevel < 1) return Property.zero
        if (uniqueLevel == 1) return baseProperty

        return if (rfGrowthProperty == null || uniqueLevel < 261) {
            Property { index ->
                baseProperty[index] + ceil(growthProperty[index] * (uniqueLevel - 1))// TODO 不确定的取整方式
            }
        } else {
            Property { index ->
                baseProperty[index] + ceil(growthProperty[index] * 259 + rfGrowthProperty[index] * (uniqueLevel - 260))// TODO 不确定的取整方式
            }
        }
    }

    fun getUnique2Property(uniqueLevel: Int): Property {
        if (uniqueLevel < 0) return Property.zero
        if (uniqueLevel == 0) return baseProperty

        return Property { index ->
            baseProperty[index] + ceil(growthProperty[index] * uniqueLevel)// TODO 不确定的取整方式
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
