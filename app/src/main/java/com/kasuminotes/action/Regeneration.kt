package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getRegeneration(skillLevel: Int, property: Property): D {
    val content: D
    val formula: D
    if (actionDetail2 == 1) {
        content = D.Format(R.string.hp)
        formula = getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, actionValue4, skillLevel, property)
    } else {
        content = D.Format(R.string.energy)
        formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
    }

    return D.Format(
        R.string.action_regeneration_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content,
            formula,
            D.Text(actionValue5.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun SkillAction.getRegenerationEffect(skillLevel: Int): SkillEffect {
    val label = D.Format(
        if (actionDetail1 == 1) R.string.effect_hp_regeneration
        else R.string.effect_energy_regeneration
    )
    return SkillEffect(
        getTarget(null),
        label,
        D.Text((actionValue1 + actionValue2 * skillLevel).toNumStr()),
        actionValue5,
        0.5f,
        SkillEffect.regeneration
    )
}
