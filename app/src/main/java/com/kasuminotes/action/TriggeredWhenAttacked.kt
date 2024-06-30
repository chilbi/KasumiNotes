package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTriggeredWhenAttacked(): D {
    return D.Format(
        R.string.action_triggered_when_attacked_target1_state2_count3_time4_cut5,
        arrayOf(
            getTarget(depend),
            getStateContent(actionValue1.toInt(), actionId),
            D.Text(actionValue2.toNumStr()),
            D.Text(actionValue4.toNumStr()),
            D.Text("${actionValue3.toNumStr()}%")
        )
    )
}
