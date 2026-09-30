package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getRegeneration(skillLevel: Int, property: Property): D {
    val content: D
    val formula: D
    when (actionDetail2) {
        2, 4 -> {
            content = if (actionDetail2 == 2) D.Format(R.string.energy) else D.Join(arrayOf(D.Format(R.string.content_fixed), D.Format(R.string.energy)))
            formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        }
        else -> {//1, 3
            content = if (actionDetail2 == 1) D.Format(R.string.hp) else D.Join(arrayOf(D.Format(R.string.content_fixed), D.Format(R.string.hp)))
            formula = getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, actionValue4, skillLevel, property)
        }
    }

    return D.Format(
        R.string.action_regeneration_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content,
            formula,
            getBaseLvFormula(actionValue5, actionValue6, skillLevel)
        )
    )
}

fun SkillAction.getRegenerationEffect(skillLevel: Int): SkillEffect {
    val label = when (actionDetail2) {
        2 -> D.Format(R.string.effect_energy_regeneration)
        3 -> D.Format(R.string.effect_fixed_hp_regeneration)
        4 -> D.Format(R.string.effect_fixed_energy_regeneration)
        else -> D.Format(R.string.effect_hp_regeneration)//1
    }

    return SkillEffect(
        getTarget(null),
        label,
        D.Text((actionValue1 + actionValue2 * skillLevel).toNumStr()),
        actionValue5 + actionValue6 * skillLevel,
        0.5f,
        SkillEffect.regeneration
    )
}
