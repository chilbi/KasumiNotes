package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAccumulativeDamage(skillLevel: Int): D {
    return D.Format(
        R.string.action_accumulative_damage_formula1_tier2,
        arrayOf(
            getBaseLvFormula(actionValue2, actionValue3, skillLevel),
            D.Text(actionValue4.toNumStr())
        )
    )
}
