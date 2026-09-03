package com.kasuminotes.data

data class UserKnightEnhance(
    val userId: Int,
    val talentLevels: List<Int/*enhanceLevel*/>,
    val talentNodes: List<Pair<Int/*nodeId*/, Int/*enhanceLevel*/>>,
    val teamNode: Int,//nodeId
    val roles: Map<Int/*roleId*/, Map<Int/*slotId*/, Pair<Int/*slotLevel*/, Int/*enhanceLevel*/>>>
) {
    val stringValues: String
        get() = buildString {
            append("$userId,")
            val rawTalentLevels = talentLevels.joinToString(",")
            append("'$rawTalentLevels',")
            val rawTalentNodes = talentNodes.joinToString(",") {
                "${it.first}:${it.second}"
            }
            append("'$rawTalentNodes',")
            append("$teamNode,")
            val rawRoles = roles
                .flatMap { roleEntry ->
                    val roleId = roleEntry.key
                    roleEntry.value.map { slotEntry ->
                        val slotId = slotEntry.key
                        val slotLevel = slotEntry.value.first
                        val enhanceLevel = slotEntry.value.second
                        "$roleId:$slotId:$slotLevel:$enhanceLevel"
                    }
                }
                .joinToString(",")
            append("'$rawRoles'")
        }
}
