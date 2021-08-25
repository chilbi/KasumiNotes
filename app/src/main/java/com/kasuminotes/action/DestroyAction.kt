package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDestroy(): D {
    return D.Format(
        R.string.action_destroy_target1,
        arrayOf(getTarget(depend))
    )
}
