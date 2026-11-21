package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getEnergyCut(): D {
    val isPercent = actionDetail1 == 0
    val value = D.Text(if (isPercent) {
        (actionValue1 * 100).toNumStr() + "%"
    } else {
        actionValue1.toNumStr()
    }).style(primary = true, bold = true)

    return D.Format(
        if (isPercent) R.string.action_energy_down_cut_target1_formula2_time3
        else R.string.action_energy_down_limit_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            value,
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun SkillAction.getEnergyCutEffect(giveValue: Double): SkillEffect {
    val isCut = actionDetail1 == 0
    val value = D.Text(if (isCut) {
        ((actionValue1 + giveValue) * 100).toNumStr() + "%"
    } else {
        (actionValue1 + giveValue).toNumStr()
    })

    return SkillEffect(
        getTarget(null),
        D.Format(if (isCut) R.string.effect_energy_down_cut else R.string.effect_energy_down_limit),
        value,
        actionValue2,
        0.5f,
        SkillEffect.energyCut
    )
}
