package com.kasuminotes.data

data class UnitRarity(
    val baseProperty: Property,
    val growthProperty: Property
) {
    companion object {
        fun getFields(): String {
            val growthFields = Property.keys.joinToString("_growth,") + "_growth"
            return Property.getFields() + "," + growthFields
        }
    }
}
