package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageField(skillLevel: Int, property: Property): D {
    val detail1 = if (actionDetail1 % 2 == 0) 2 else 1
    var content: D = D.Format(if (detail1 == 1) R.string.physical else R.string.magic)
    if (actionDetail1 > 4) {
        content = content.insert(D.Format(R.string.content_lower_defense))
    }
    return D.Format(
        R.string.action_damage_field_target1_range2_formula3_content4_time5,
        arrayOf(
            getTarget(depend, true),
            D.Text(actionValue7.toNumStr()),
            getBaseLvAtkFormula(detail1, actionValue1, actionValue2, actionValue3, actionValue4, skillLevel, property),
            content,
            D.Text(actionValue5.toNumStr())
        )
    )
}
