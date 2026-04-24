package com.kasuminotes.data

data class TeamSkillNode(
    val nodeId: Int,
    val iconId: Int,
    val titleId: Int,
    val posX: Int,
    val posY: Int,
    val enhanceLevelList: List<SkillEnhanceLevel>,//1-5
    val preNodeId: Int,
    var preNode: TeamSkillNode?
)
