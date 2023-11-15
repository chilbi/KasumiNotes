package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTotalCritical(): D {
    return D.Format(R.string.action_total_critical_target1, arrayOf(getTarget(depend)))
}
