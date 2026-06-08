package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getForceChangeSpeed(skillLevel: Int): D {
    return getChangeSpeed(skillLevel).append(D.Format(R.string.action_force_change_speed))
}
