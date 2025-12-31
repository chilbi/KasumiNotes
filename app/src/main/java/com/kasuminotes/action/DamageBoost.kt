package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageBoost(skillLevel: Int): D {
    val content = D.Format(
        R.string.action_up_target1_content2_formula3,
        arrayOf(
            getTarget(depend),
            D.Format(R.string.damage_boost).style(underline = true),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel).append(D.Text("%").style(primary = true, bold = true)),
        )
    )
    return D.Format(
        R.string.action_damage_boost_content1_max2_time3,
        arrayOf(
            content,
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue4.toNumStr()).style(primary = true, bold = true)
        )
    )
}
