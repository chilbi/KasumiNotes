package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalDamage(skillLevel: Int): D {
    val target = getTarget(depend)
    val content = getAbnormalDamageContent(actionDetail1)
    val formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
    val time = D.Text(actionValue3.toNumStr())

    return if (actionValue5 == 0.0) {
        D.Format(
            R.string.action_abnormal_target1_content2_formula3_time4,
            arrayOf(target, content, formula, time)
        )
    } else {
        val percent = D.Text("${actionValue5.toNumStr()}%")
        D.Format(
            R.string.action_abnormal_target1_content2_formula3_percent4_time5,
            arrayOf(target, content, formula, percent, time)
        )
    }
}
