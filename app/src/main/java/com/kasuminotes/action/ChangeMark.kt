package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.absoluteValue

fun SkillAction.getChangeMark(skillLevel: Int, actions: List<SkillAction>): D {
    val target = getTarget(depend)
    val state = getMarkContent(actionValue2.toInt())
    val time = D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)

    return if (actionValue1 > 1.0) {
        val isConsume = if (actionValue4 < 0.0) {
            true
        } else {
            if (actionValue4 == 0.0) {
                val giveValueAction = actions.find { it.actionType == 26 && it.actionDetail1 == actionId }
                if (giveValueAction == null) {
                    false
                } else {
                    if ((giveValueAction.actionValue2 + giveValueAction.actionValue3 * skillLevel) < 0.0) {
                        true
                    } else {
                        false
                    }
                }
            } else {
                false
            }
        }
        if (isConsume) {
            D.Format(
                R.string.action_change_mark_target1_state2_consume3,
                arrayOf(target, state, D.Text(actionValue4.absoluteValue.toNumStr()).style(primary = true, bold = true))
            )
        } else {
            val add = D.Text(actionValue4.toNumStr()).style(primary = true, bold = true)
            val max = D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
            if (actionValue5 < 1.0) {
                D.Format(
                    R.string.action_change_mark_target1_state2_add3_max4_time5,
                    arrayOf(target, state, add, max, time)
                )
            } else {
                D.Format(
                    R.string.action_change_mark_target1_state2_add3_max4_sub5_time6,
                    arrayOf(target, state, add, max, D.Text(actionValue5.toNumStr()).style(primary = true, bold = true), time)
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
