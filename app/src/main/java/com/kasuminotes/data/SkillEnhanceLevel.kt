package com.kasuminotes.data

data class SkillEnhanceLevel(
    val enhanceLevelId: Int,
    val itemId: Int,
    val consumeNum: Int,
    val enhanceDataList: List<SkillEnhanceData>//1-5
)
