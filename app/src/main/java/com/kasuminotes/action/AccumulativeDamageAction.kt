package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAccumulativeDamage(skillLevel: Int): D {
    var formula = getBaseLvFormula(actionValue2, actionValue3, skillLevel)
    if (actionValue1 == 2.0) {
        formula = formula.append(D.Text("%"))
    }

    return D.Format(
        R.string.action_accumulative_damage_formula1_tier2,
        arrayOf(
            formula,
            D.Text(actionValue4.toNumStr())
        )
    )
}
