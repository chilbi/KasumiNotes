package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageUp(skillLevel: Int): D {
    val content = when (actionDetail1) {
        10 -> D.Format(R.string.content_ub_damage)
        20 -> D.Format(R.string.content_skill_damage)
        else -> D.Text("???").append(D.Format(R.string.damage))
    }.style(underline = true)
    val formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        .append(D.Text("%").style(primary = true, bold = true))
    val time = getBaseLvFormula(actionValue3, actionValue4, skillLevel)
    return D.Format(
        R.string.action_up_target1_content2_formula3,
        arrayOf(getTarget(depend), content, formula)
    ).append(D.Format(R.string.action_time1, arrayOf(time)))
}
