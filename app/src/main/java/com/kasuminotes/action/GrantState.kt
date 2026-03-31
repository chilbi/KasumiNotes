package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getGrantState(): D {
    return when (actionDetail1) {
        3 -> D.Format(
            R.string.action_grant_must_critical_target1_state2_time3,
            arrayOf(
                getTarget(depend),
                getMarkContent(actionDetail2),
                D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
            )
        )
        else -> getUnknown()
    }
}
