package com.kasuminotes.data

data class TalentSkillNode(
    val nodeId: Int,
    val iconId: Int,
    val titleId: Int,
    val pageNum: Int,
    val posX: Int,
    val posY: Int,
    val enhanceLevelList: List<SkillEnhanceLevel>,//1-5
    val preNodeIdList: List<Int>,//1-5
    var preNodeList: List<TalentSkillNode>//1-5
) {
    val maxLevel: Int = enhanceLevelList.size
}
