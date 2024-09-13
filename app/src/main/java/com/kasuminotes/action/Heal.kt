package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getHeal(skillLevel: Int, property: Property): D {
    val formula = if (actionValue1 == 1.0) {
        getBaseLvAtkFormula(actionDetail1, actionValue2, actionValue3, actionValue4, actionValue5, skillLevel, property)
    } else {
        D.Text("${actionValue2.toNumStr()}%").style(primary = true, bold = true)
    }

    return D.Format(
        R.string.action_hp_recovery_target1_formula2,
        arrayOf(
            getTarget(depend),
            formula
        )
    )
}
