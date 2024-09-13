package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getNoDamage(skillLevel: Int): D {
    val content = when (actionDetail1) {
        1 -> D.Format(R.string.invincible)
        2 -> D.Format(R.string.physical_invalid)
        6 -> D.Format(R.string.break_immunity)
        8 -> D.Format(R.string.magic_invalid)
        else -> D.Unknown
    }

    return D.Format(
        R.string.action_state_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            content.style(underline = true),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel) { it }
        )
    )
}
