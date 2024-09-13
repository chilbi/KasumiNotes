package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.absoluteValue

fun SkillAction.getMove(): D {
    return when (actionDetail1) {
        1 -> {
            D.Join(
                arrayOf(
                    D.Format(
                        R.string.action_move_target1,
                        arrayOf(getTarget(depend))
                    ),
                    D.Format(R.string.action_move_return)
                )
            )
        }
        3 -> {
            val target = getTarget(depend)
            if (actionValue1 > 0.0) {
                D.Format(
                    R.string.action_move_forward_target1_distance2,
                    arrayOf(target, D.Text(actionValue1.toNumStr()).style(primary = true, bold = true))
                )
            } else if (actionValue1 < 0.0) {
                D.Format(
                    R.string.action_move_backward_target1_distance2,
                    arrayOf(target, D.Text(actionValue1.absoluteValue.toNumStr()).style(primary = true, bold = true))
                )
            } else {
                D.Format(
                    R.string.action_move_target1,
                    arrayOf(getTarget(depend))
                )
            }
        }
        5 -> {
            D.Format(
                R.string.action_move_velocity1_target2_distance3,
                arrayOf(
                    D.Text(actionValue2.toNumStr()).style(primary = true, bold = true),
                    getTarget(depend),
                    D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
                )
            )
        }
        7 -> {
            D.Format(
                if (actionValue1 > 0.0) R.string.action_move_forward_distance1
                else R.string.action_move_backward_distance1,
                arrayOf(D.Text(actionValue1.absoluteValue.toNumStr()).style(primary = true, bold = true))
            )
        }
        else -> getUnknown()
    }
}
