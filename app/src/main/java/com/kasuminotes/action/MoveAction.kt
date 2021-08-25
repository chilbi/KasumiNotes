package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

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
            D.Format(
                R.string.action_move_target1,
                arrayOf(getTarget(depend))
            )
        }
        5 -> {
            D.Format(
                R.string.action_move_velocity1_target2_distance3,
                arrayOf(
                    D.Text(actionValue2.toNumStr()),
                    getTarget(depend),
                    D.Text(actionValue1.toNumStr())
                )
            )
        }
        else -> getUnknown()
    }
}
