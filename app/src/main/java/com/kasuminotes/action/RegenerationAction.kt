package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getRegeneration(skillLevel: Int, property: Property): D {
    val content: D
    val formula: D
    if (actionDetail2 == 1) {
        content = D.Format(R.string.hp)
        formula = getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, skillLevel, property)
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
            D.Text(actionValue5.toNumStr())
        )
    )
}
