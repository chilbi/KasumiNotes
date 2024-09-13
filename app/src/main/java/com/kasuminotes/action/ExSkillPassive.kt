package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getExSkillPassive(skillLevel: Int): D {
    val index = actionDetail1 - 1
    return D.Format(
        R.string.action_ex_passive_content1_formula2_time3,
        arrayOf(
            D.Format(if (index == 0) R.string.max_hp else Property.getStrRes(index)).style(underline = true),
            getBaseLvFormula(actionValue2, actionValue3, skillLevel),
            D.Text("90").style(primary = true, bold = true)
        )
    )
}
