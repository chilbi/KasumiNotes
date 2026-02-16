package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEndure(): D {
    return D.Format(
        R.string.action_endure_target1_state2_formula3_time4,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionDetail2),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue5.toNumStr()).style(primary = true, bold = true)
        )
    )
}
