package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEnergyRecoveryRateCeilingRestriction(skillLevel: Int): D {
    return D.Format(
        R.string.action_energy_recovery_rate_ceiling_restriction_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
