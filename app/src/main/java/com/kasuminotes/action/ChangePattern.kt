package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangePattern(): D {
    val pattern = D.Text((actionDetail2 % 10).toString()).style(primary = true, bold = true)

    return if (actionValue1 < 0) {
        D.Format(
            R.string.action_mode_pattern1,
            arrayOf(pattern)
        )
    } else {
        D.Format(
            R.string.action_mode_pattern1_time2,
            arrayOf(
                pattern,
                D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
            )
        )
    }
}
