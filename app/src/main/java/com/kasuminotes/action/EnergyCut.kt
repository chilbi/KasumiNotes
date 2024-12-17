package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getEnergyCut(): D {
    return D.Format(
        R.string.action_energy_cut_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${(actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun SkillAction.getEnergyCutEffect(giveValue: Double): SkillEffect {
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_energy_cut),
        D.Text("${((actionValue1 + giveValue) * 100).toNumStr()}%"),
        actionValue2,
        0.5f,
        69
    )
}
