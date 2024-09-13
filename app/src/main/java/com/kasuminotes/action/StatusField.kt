package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getStatusField(skillLevel: Int): D {
    var formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
    if (actionDetail2 == 2) {
        formula = formula.append(D.Text("%").style(primary = true, bold = true))
    }

    val content = D.Format(
        if (actionDetail1 % 10 == 0) R.string.content_buff_target1_content2_formula3
        else R.string.content_debuff_target1_content2_formula3,
        arrayOf(
            getAssignment(),
            getStatusContent(actionDetail1 / 10).style(underline = true),
            formula
        )
    )

    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue5.toNumStr()),
            content,
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
