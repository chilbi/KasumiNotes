package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageField(skillLevel: Int, property: Property): D {
    return D.Format(
        R.string.action_damage_field_target1_range2_formula3_content4_time5,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue7.toNumStr()),
            getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, skillLevel, property),
            D.Format(if (actionDetail1 == 1 || actionDetail1 == 3) R.string.physical else R.string.magic),
            D.Text(actionValue5.toNumStr())
        )
    )
}
