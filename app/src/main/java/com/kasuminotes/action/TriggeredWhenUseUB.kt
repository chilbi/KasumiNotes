package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTriggeredWhenUseUB(): D {
    return D.Format(
        R.string.action_triggered_when_use_ub_target1_state2_count3_max4_time5,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionValue1.toInt()),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue4.toNumStr()).style(primary = true, bold = true),
        )
    )
}
