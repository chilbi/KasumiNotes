package com.kasuminotes.data

data class UnitPromotion(
    val equipSlot1: EquipData?,
    val equipSlot2: EquipData?,
    val equipSlot3: EquipData?,
    val equipSlot4: EquipData?,
    val equipSlot5: EquipData?,
    val equipSlot6: EquipData?
) {
    val equipSlots: List<EquipData?>
        get() = listOf(equipSlot1, equipSlot2, equipSlot3, equipSlot4, equipSlot5, equipSlot6)

    fun getEquip(slot: Int): EquipData? = when (slot) {
        1 -> equipSlot1
        2 -> equipSlot2
        3 -> equipSlot3
        4 -> equipSlot4
        5 -> equipSlot5
        else -> equipSlot6
    }

    companion object {
        fun getFields(): String {
            return (1..6).joinToString(",") { i -> "equip_slot_$i" }
        }
    }
}
