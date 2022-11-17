package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getExPassive(skillLevel: Int): D {
    return D.Format(
        R.string.action_ex_passive_content1_formula2,
        arrayOf(
            D.Format(Property.getStrRes(actionDetail1 - 1)),
            getBaseLvFormula(actionValue2, actionValue3, skillLevel),
        )
    )
}
