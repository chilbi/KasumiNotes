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
) {
    companion object {
        const val damageAttenuation = 999//受到的伤害衰减
        const val energyCut = 399//受到的TP降低效果
        const val damageCut = 398//受到的物理/魔法伤害降低
        const val energyRestriction = 299//TP上升的上限
        const val status = 100//属性值80-100，固定防御力*10
        const val abnormal = 69//行动速度
        const val hpRecoveryDown = 4//HP回复量降低
        const val changeEnergy = 3//TP回复
        const val regeneration = 2//持续HP/TP回复
        const val abnormalDamage = 1//持续伤害
        const val unknownType = 0//未知
    }
}
