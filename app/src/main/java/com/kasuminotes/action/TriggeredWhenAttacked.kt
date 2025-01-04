package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTriggeredWhenAttacked(): D {
    return D.Format(
        R.string.action_triggered_when_attacked_target1_state2_count3_time4_cut5,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionValue1.toInt()),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue4.toNumStr()).style(primary = true, bold = true),
            D.Text("${actionValue3.toNumStr()}%").style(primary = true, bold = true)
        )
    )
}
