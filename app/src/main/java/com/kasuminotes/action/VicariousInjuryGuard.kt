package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getVicariousInjuryGuard(): D {
    return D.Format(
        R.string.action_vicarious_injury_guard_target1_state2_time3,
        arrayOf(
            getTarget(depend),
            getStateContent(actionDetail1, actionId),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
