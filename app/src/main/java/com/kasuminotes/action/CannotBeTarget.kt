package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getCannotBeTarget(): D {
    return D.Format(
        R.string.action_cannot_be_target1_state2,
        arrayOf(getTarget(depend), getStateContent(actionDetail2, actionId))
    )
}
