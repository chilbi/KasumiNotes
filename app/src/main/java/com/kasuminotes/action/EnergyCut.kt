package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getEnergyCut(): D {
    val isCut = actionDetail1 == 0
    val value = if (isCut) actionValue1 * 100 else actionValue1

    return D.Format(
        if (isCut) R.string.action_energy_down_cut_target1_formula2_time3
        else R.string.action_energy_down_limit_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${value.toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun SkillAction.getEnergyCutEffect(giveValue: Double): SkillEffect {
    val isCut = actionDetail1 == 0
    val value = if (isCut) (actionValue1 + giveValue) * 100 else actionValue1 + giveValue

    return SkillEffect(
        getTarget(null),
        D.Format(if (isCut) R.string.effect_energy_down_cut else R.string.effect_energy_down_limit),
        D.Text("${value.toNumStr()}%"),
        actionValue2,
        0.5f,
        SkillEffect.energyCut
    )
}
