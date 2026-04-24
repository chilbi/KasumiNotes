package com.kasuminotes.data

data class RoleMasteryData(
    val masteryId: Int,
    val unitRoleId: Int,
    val slotIdMap: Map<Int, RoleSlotData>//slotId
)

data class RoleSlotData(
    val slotId: Int,
    val slotLevelMap: Map<Int, RoleSlotLevel>//slotLevel
) {
    val maxSlotLevel = slotLevelMap.size
}

data class RoleSlotLevel(
    val slotLevel: Int,
    val iconId: Int,
    val enhanceLevelMap: Map<Int, RoleEnhanceLevel>//enhanceLevel
) {
    val maxEnhanceLevel = enhanceLevelMap.size - 1
}

data class RoleEnhanceLevel(
    val enhanceLevel: Int,
    val itemId: Int,
    val num: Int,
    val typeValuePairList: List<Pair<Int, Int>>//roleParamType to roleEnhanceValue
)
