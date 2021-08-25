package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRatioDamage(): D {
    return D.Format(
        R.string.action_damage_target1_ratio2,
        arrayOf(
            getTarget(depend),
            D.Text("${actionValue1.toNumStr()}%")
        )
    )
}
