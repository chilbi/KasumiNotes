package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getKnightGuard(skillLevel: Int, property: Property): D {
    return D.Format(
        R.string.action_knight_guard_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            getBaseLvAtkFormula(actionDetail1, actionValue2, actionValue3, actionValue4, actionValue5, skillLevel, property),
            D.Text(actionValue6.toNumStr())
        )
    )
}
