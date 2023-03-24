package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getCountDown(): D {
    return D.Format(
        R.string.action_count_down_target1_count2,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue1.toNumStr())
        )
    )
}
