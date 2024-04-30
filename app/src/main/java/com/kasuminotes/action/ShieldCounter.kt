package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getShieldCounter(skillLevel: Int): D {
    return D.Format(
        R.string.action_shield_counter_target1_content2_add3_formula4,
        arrayOf(
            getTarget(depend),
            getStateContent(actionDetail2, actionId),
            D.Text(actionValue3.toNumStr()),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        )
    )
}
