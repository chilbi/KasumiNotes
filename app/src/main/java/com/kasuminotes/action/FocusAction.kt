package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFocus(): D {
    return D.Format(
        R.string.action_focus_target1,
        arrayOf(getTarget(null))
    )
}
