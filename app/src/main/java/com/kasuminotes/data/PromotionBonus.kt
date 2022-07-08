package com.kasuminotes.data

data class PromotionBonus(
    val promotionLevel: Int,
    val bonusProperty: Property
) {
    companion object {
        fun getFields(): String  = "${Property.getFields()},promotion_level"
    }
}
