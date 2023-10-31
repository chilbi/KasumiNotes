package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFallenAngelGuard(): D {
    return D.Format(
        R.string.action_fallen_angel_guard_target1_time2,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue3.toNumStr())
        )
    )
}
