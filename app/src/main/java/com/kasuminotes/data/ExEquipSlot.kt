package com.kasuminotes.data

data class ExEquipSlot(
    val category: Int,
    val exEquipData: ExEquipData?,
    val exEquipCategory: ExEquipCategory? = null,
    val equippableExList: List<Int> = emptyList()
)
