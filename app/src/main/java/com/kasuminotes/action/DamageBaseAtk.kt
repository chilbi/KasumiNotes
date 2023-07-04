package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageBaseAtk(): D {
    return D.Format(
        R.string.action_damage_base_atk1_target2,
        arrayOf(
            getAtkType(actionDetail1),
            getTarget(depend)
        )
    )
}
