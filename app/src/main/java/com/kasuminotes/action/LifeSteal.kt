package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getLifeSteal(skillLevel: Int): D {
    return D.Format(
        R.string.action_life_steal_target1_formula2,
        arrayOf(
            getTarget(depend),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        )
    )
}
