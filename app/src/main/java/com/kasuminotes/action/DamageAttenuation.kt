package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect
import kotlin.math.ln

fun SkillAction.getDamageAttenuation(skillLevel: Int): D {
    val limit = actionValue5 / 10000
    val n4 = if (limit > 100.0) limit + 100.0 else 100.0
    val n6 = n4 + 100.0
    val n8 = 999.0
    val breakpoint = actionValue5 / 10000
    val attenuationCoefficient = (actionValue1 + actionValue2 * skillLevel) / 10000

    return D.Format(
        R.string.action_damage_attenuation_target1_limit2_time3_n4_r5_n6_r7_n8_r9,
        arrayOf(
            getTarget(depend),
            D.Text(limit.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(n4.toNumStr()),
            getDamageText(n4, breakpoint, attenuationCoefficient),
            D.Text(n6.toNumStr()),
            getDamageText(n6, breakpoint, attenuationCoefficient),
            D.Text(n8.toNumStr()),
            getDamageText(n8, breakpoint, attenuationCoefficient)
        )
    )
}

fun SkillAction.getDamageAttenuationEffect(skillLevel: Int): SkillEffect {
    val breakpoint = actionValue5 / 10000
    val attenuationCoefficient = (actionValue1 + actionValue2 * skillLevel) / 10000
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_damage_attenuation),
        D.Format(
            R.string.effect_damage_attenuation_limit1_convergence2,
            arrayOf(
                D.Text(breakpoint.toNumStr()),
                getDamageText(999.0, breakpoint, attenuationCoefficient)
            )
        ),
        actionValue3,
        1f,
        SkillEffect.damageAttenuation
    )
}

private fun getDamageText(damage: Double, breakpoint: Double, attenuationCoefficient: Double): D {
    val result = damage * ((attenuationCoefficient * ln((damage - breakpoint) / attenuationCoefficient + 1) + breakpoint) / damage)
    return D.Text(result.toInt().toString())
}
