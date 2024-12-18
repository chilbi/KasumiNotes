package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

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

fun SkillAction.getChangeEnergyEffect(skillLevel: Int): SkillEffect {
    val label = when (actionDetail1) {
        1 -> D.Format(R.string.effect_energy_recovery)
        4 -> D.Join(arrayOf(D.Format(R.string.content_fixed), D.Format(R.string.effect_energy_recovery)))
        else -> D.Format(R.string.effect_energy_down)
    }
    return SkillEffect(
        getTarget(null),
        label,
        D.Text((actionValue1 + actionValue2 * skillLevel).toNumStr()),
        0.0,
        0.5f,
        SkillEffect.changeEnergy
    )
}
