package com.kasuminotes.data

data class EquipInfo(
    val equipmentId: Int,
    val type: Int
) {
    companion object {
        var typePairList: List<Pair<Int, String>>? = null
    }
}
