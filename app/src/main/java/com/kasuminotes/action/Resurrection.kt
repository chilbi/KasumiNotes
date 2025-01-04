package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getResurrection(skillLevel: Int, property: Property): D {
    return D.Format(
        R.string.action_resurrection_target1_state2_formula3_time4,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionDetail2),
            getBaseLvAtkFormula(actionDetail1, actionValue2, actionValue3, actionValue4, actionValue5, skillLevel, property),
            D.Text(actionValue6.toNumStr()).style(primary = true, bold = true)
        )
    )
}
