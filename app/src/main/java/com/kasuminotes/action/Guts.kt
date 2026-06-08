package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction

fun SkillAction.getGuts(skillLevel: Int, property: Property): D {
    val target = getTarget(depend)
    val state = getMarkContent(actionDetail2)
    val time = D.Text(actionValue5.toNumStr()).style(primary = true, bold = true)
    val formula = if (actionValue1 == 1.0) {
        if (actionValue2 == 0.0 && actionValue3 == 0.0 && actionValue4 == 0.0) {
            D.Text("1").style(primary = true, bold = true)
        } else {
            getBaseLvAtkFormula(actionDetail1, actionValue2, actionValue3, actionValue4, 0.0, skillLevel, property)
        }
    } else {//2.0
        getBaseLvAtkFormula(actionDetail1, actionValue2, actionValue3, actionValue4, 0.0, skillLevel, property)
            .append(D.Text("%").style(primary = true, bold = true))
    }
    return if (actionValue6 == 0.0) {
        D.Format(
            R.string.action_guts_target1_state2_formula3_time4,
            arrayOf(target, state, formula, time)
        )
    } else if (actionValue7 == 0.0) {
        D.Format(
            R.string.action_guts_limited_target1_state2_formula3_time4,
            arrayOf(target, state, formula, time)
        )
    } else {
        val count = D.Text(actionValue7.toNumStr()).style(primary = true, bold = true)
        D.Format(
            R.string.action_guts_limited_target1_state2_formula3_time4_count5,
            arrayOf(target, state, formula, time, count)
        )
    }
}
