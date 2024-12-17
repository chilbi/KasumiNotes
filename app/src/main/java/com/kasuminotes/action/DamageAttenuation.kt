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
    val s = actionValue5 / 10000
    val m = (actionValue1 + actionValue2 * skillLevel) / 10000

    return D.Format(
        R.string.action_damage_attenuation_target1_limit2_time3_n4_r5_n6_r7_n8_r9,
        arrayOf(
            getTarget(depend),
            D.Text(limit.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(n4.toNumStr()),
            getDamageText(n4, s, m),
            D.Text(n6.toNumStr()),
            getDamageText(n6, s, m),
            D.Text(n8.toNumStr()),
            getDamageText(n8, s, m)
        )
    )
}

fun SkillAction.getDamageAttenuationEffect(skillLevel: Int): SkillEffect {
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_damage_attenuation),
        D.Format(
            R.string.effect_damage_attenuation_limit1_coefficient2,
            arrayOf(
                D.Text((actionValue5 / 10000).toNumStr()),
                D.Text(((actionValue1 + actionValue2 * skillLevel) / 10000).toNumStr())
            )
        ),
        actionValue3,
        1f,
        9999
    )
}

private fun getDamageText(d: Double, s: Double, m: Double): D {
    val damage = d * ((m * ln((d - s) / m + 1) + s) / d)
    return D.Text(damage.toInt().toString())
}
