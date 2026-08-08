package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEffectResistance(): D {
    val content: D = if (actionDetail1 == 2 && actionDetail2 == 1) {
        D.Join(arrayOf(
            D.Format(R.string.magic_str),
            D.Format(R.string.content_down),
            D.Format(R.string.comma),
            D.Format(R.string.def),
            D.Format(R.string.content_down),
            D.Format(R.string.comma),
            D.Format(R.string.magic_def),
            D.Format(R.string.content_down)
        )).style(underline = true)
    } else {
        D.Unknown
    }
    return D.Format(
        R.string.action_effect_resistance_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            content,
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
