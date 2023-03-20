package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFocus(modifyAction: SkillAction): D {
    return D.Format(
        if (modifyAction.targetArea == 1) {
            R.string.action_focus_front_target1
        } else {
            R.string.action_focus_target1
        },
        arrayOf(getTarget(null))
    )
}
