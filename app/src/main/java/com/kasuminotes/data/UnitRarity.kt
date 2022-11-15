package com.kasuminotes.data

data class UnitRarity(
    val baseProperty: Property,
    val growthProperty: Property
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val growthFields = Property.keys.joinToString("_growth,") + "_growth"
                fields = Property.getFields() + "," + growthFields
            }
            return fields!!
        }
    }
}
