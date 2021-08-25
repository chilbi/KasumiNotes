package com.kasuminotes.data

data class UnitPromotionStatus(
    val baseProperty: Property
) {
    companion object {
        fun getFields(): String  = Property.getFields()
    }
}
