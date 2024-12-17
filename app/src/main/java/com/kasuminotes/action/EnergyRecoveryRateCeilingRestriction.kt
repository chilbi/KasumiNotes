package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

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

fun SkillAction.getEnergyRecoveryRateCeilingRestrictionEffect(skillLevel: Int): SkillEffect {
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_energy_recovery_rate_ceiling_restriction),
        D.Text((actionValue1 + actionValue2 * skillLevel).toNumStr()),
        actionValue3,
        0.5f,
        60
    )
}
