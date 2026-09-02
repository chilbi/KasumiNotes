package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEffectResistance(): D {
    val content = getResistance("$actionDetail1,$actionDetail2,$actionDetail3")
    return D.Format(
        R.string.action_effect_resistance_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            content.style(underline = true),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
