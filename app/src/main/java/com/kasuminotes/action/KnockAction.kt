package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getKnock(): D {
    val target = getTarget(depend)
    return when (actionDetail1) {
        1 -> {
            D.Format(
                R.string.action_diaup_target1_formula2,
                arrayOf(
                    target,
                    D.Text(actionValue1.toNumStr())
                )
            )
        }
        3, 9 -> {
            if (actionValue1 > 0) {
                D.Format(
                    R.string.action_knockback_target1_formula2,
                    arrayOf(
                        target,
                        D.Text(actionValue1.toNumStr())
                    )
                )
            } else {
                D.Format(
                    R.string.action_haulin_target1_formula2,
                    arrayOf(
                        target,
                        D.Text((-actionValue1).toNumStr())
                    )
                )
            }
        }
        8 -> {
            D.Format(
                R.string.action_haulin_oneself_target1_formula2,
                arrayOf(
                    target,
                    D.Text(actionValue1.toNumStr())
                )
            )
        }
        else -> getUnknown()
    }
}
