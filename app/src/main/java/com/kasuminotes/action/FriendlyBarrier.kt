package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFriendlyBarrier(): D {
    return D.Format(
        R.string.action_friendly_barrier_target1_state2_time3,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionDetail1),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
