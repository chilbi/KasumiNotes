package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.ln

fun SkillAction.getDamageAttenuation(): D {
    val s = actionValue5 / 10000
    val m = actionValue1 / 10000

    return D.Format(
        R.string.action_damage_attenuation_target1_limit2_time3_r4_r5_r6,
        arrayOf(
            getTarget(depend),
            D.Text((actionValue5 / 10000).toNumStr()),
            D.Text(actionValue3.toNumStr()),
            getDamageText(100.0, s, m),
            getDamageText(200.0, s, m),
            getDamageText(999.0, s, m)
        )
    )
}

private fun getDamageText(d: Double, s: Double, m: Double): D {
    val damage = d * ((m * ln((d - s) / m + 1) + s) / d)
    return D.Text(damage.toInt().toString())
}
