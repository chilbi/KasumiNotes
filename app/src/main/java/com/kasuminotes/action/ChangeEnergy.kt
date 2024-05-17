package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeEnergy(skillLevel: Int): D {
    return D.Format(
        when (actionDetail1) {
            1 -> R.string.action_energy_recovery_target1_formula2
            4 -> R.string.action_energy_recovery_fixed_target1_formula2
            else -> R.string.action_energy_down_target1_formula2
        },
        arrayOf(
            getTarget(depend),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        )
    )
}
