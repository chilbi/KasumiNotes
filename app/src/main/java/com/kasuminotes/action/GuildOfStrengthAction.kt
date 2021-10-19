package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getGuildOfStrength(): D {
    return D.Format(
        R.string.action_strength_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${(actionValue1 * 100).toNumStr()}%"),
            D.Text(actionValue3.toNumStr())
        )
    )
}
