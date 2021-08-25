package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEnergy(skillLevel: Int): D {
    return D.Format(
        if (actionDetail1 == 1) R.string.action_energy_recovery_target1_formula2
        else R.string.action_energy_down_target1_formula2,
        arrayOf(
            getTarget(depend),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        )
    )
}
