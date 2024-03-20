package com.kasuminotes.data

import kotlin.math.ceil

data class UniqueData(
    val equipmentId: Int,
    val equipmentName: String,
    val description: String,
    val baseProperty: Property,
    val growthProperties: List<Property>,
    val growthLvRanges: List<LvRange>
) {
    fun getUnique1Property(uniqueLevel: Int): Property {
        if (uniqueLevel < 1) return Property.zero
        if (uniqueLevel == 1) return baseProperty

        return Property { index ->
            var growthValue = 0.0
            if (growthProperties.isNotEmpty()) {
                var i = 0
                val size = growthProperties.size
                do {
                    val growthProperty = growthProperties[i]
                    val growthLvRange = growthLvRanges[i]
                    if (uniqueLevel >= growthLvRange.minLv) {
                        val maxLv = if (growthLvRange.maxLv == -1 || uniqueLevel < growthLvRange.maxLv) uniqueLevel
                        else growthLvRange.maxLv
                        val lv = maxLv - growthLvRange.minLv + 1
                        growthValue += growthProperty[index] * lv
                    }
                    i++
                } while (i < size && growthLvRange.maxLv != -1 && uniqueLevel > growthLvRange.maxLv)
            }
            baseProperty[index] + ceil(growthValue)// TODO 不确定的取整方式
        }
    }

    fun getUnique2Property(uniqueLevel: Int): Property {
        if (uniqueLevel < 0) return Property.zero
        if (uniqueLevel == 0) return baseProperty
        val growthProperty = if (growthProperties.isEmpty()) Property.zero else growthProperties[0]
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
