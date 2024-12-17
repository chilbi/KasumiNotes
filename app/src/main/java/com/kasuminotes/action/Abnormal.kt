package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormal(skillLevel: Int): D {
    val time = getBaseLvFormula(actionValue3, actionValue4, skillLevel)

    return if (actionDetail1 == 1 || actionDetail1 == 2) {
        val formula = getBaseLvFormula(actionValue1 * 100, actionValue2 * 100, skillLevel)
            .append(D.Text("%").style(primary = true, bold = true))
        D.Format(
            R.string.action_speed_target1_formula2_time3,
            arrayOf(getTarget(depend), formula, time)
        )
    } else {
        val abnormal = D.Format(
            R.string.action_abnormal_target1_content2_time3,
            arrayOf(
                getTarget(depend),
                getAbnormalContent(actionDetail1).style(underline = true),
                time
            )
        )
        if (actionDetail2 == 1) {
            D.Format(
                R.string.harmed_remove_content1,
                arrayOf(abnormal)
            )
        } else {
            abnormal
        }
    }
}
