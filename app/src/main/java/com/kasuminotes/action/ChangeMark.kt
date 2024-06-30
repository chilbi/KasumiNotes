package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeMark(): D {
    val target = if (depend != null && depend!!.actionType == 1)
        D.Format(R.string.target_attacking_enemy)
    else getTarget(depend)
    val state = getStateContent(actionValue2.toInt(), actionId)
    val time = D.Text(actionValue3.toNumStr())

    return if (actionValue1 > 1.0) {
        if (actionValue4 < 0.0) {
            D.Format(
                R.string.action_change_mark_target1_state2_consume3,
                arrayOf(target, state, D.Text((-actionValue4).toNumStr()))
            )
        } else {
            val add = D.Text(actionValue4.toNumStr())
            val max = D.Text(actionValue1.toNumStr())
            if (actionValue5 < 1.0) {
                D.Format(
                    R.string.action_change_mark_target1_state2_add3_max4_time5,
                    arrayOf(target, state, add, max, time)
                )
            } else {
                D.Format(
                    R.string.action_change_mark_target1_state2_add3_max4_sub5_time6,
                    arrayOf(target, state, add, max, D.Text(actionValue5.toNumStr()), time)
                )
            }
        }
    } else {
        if (actionValue4 < 0.0) {
            D.Format(
                R.string.action_change_mark_consume_target1_state2,
                arrayOf(target, state)
            )
        } else {
            D.Format(
                R.string.action_change_mark_target1_state2_time3,
                arrayOf(target, state, time)
            )
        }
    }
}
