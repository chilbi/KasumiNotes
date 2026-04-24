package com.kasuminotes.data

data class SkillEnhanceData(
    val enhanceId: Int,
    val parameterType: Int,
    val enhanceValue: Int,
    val talentIdList: List<Int>//1-5
)
