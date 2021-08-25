package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getHealField(skillLevel: Int, property: Property): D {
    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue7.toNumStr()),
            D.Format(
                R.string.content_regeneration_hp_formula1,
                arrayOf(
                    getBaseLvAtkFormula(actionDetail1, actionValue1, actionValue2, actionValue3, skillLevel, property)
                )
            ),
            D.Text(actionValue5.toNumStr()),
        )
    )
}
