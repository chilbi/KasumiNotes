package com.kasuminotes.data

import com.kasuminotes.action.D

data class SkillEffect(
    val target: D,
    val label: D,
    val value: D,
    val duration: Double,
    var weight: Float,
    val order: Int,
    var targetText: String = "",
    var labelText: String = "",
    var valueText: String = ""
)
