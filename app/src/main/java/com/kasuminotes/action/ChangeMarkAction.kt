package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeMark(): D {
    val target = getTarget(depend)
    val state = getStateContent(actionValue2.toInt())
    val time = D.Text(actionValue3.toNumStr())

    return if (actionValue1 > 1.0) {
        if (actionValue4 > 0.0) {
            D.Format(
                R.string.action_change_mark_target1_state2_add3_max4_time5,
                arrayOf(
                    target,
                    state,
                    D.Text(actionValue4.toNumStr()),
                    D.Text(actionValue1.toNumStr()),
                    time
                )
            )
        } else {
            D.Format(
                R.string.action_change_mark_target1_state2_consume3,
                arrayOf(target, state, D.Text((-actionValue4).toNumStr()))
            )
        }
    } else {
        if (actionValue4 > 0.0) {
            D.Format(
                R.string.action_change_mark_target1_state2_time3,
                arrayOf(target, state, time)
            )
        } else {
            D.Format(
                R.string.action_change_mark_consume_target1_state2,
                arrayOf(target, state)
            )
        }
    }
}
