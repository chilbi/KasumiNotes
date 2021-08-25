package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormal(): D {
    return if (actionDetail1 == 1 || actionDetail1 == 2) {
        D.Format(
            R.string.action_speed_target1_formula2_time3,
            arrayOf(
                getTarget(depend),
                D.Text("${(actionValue1 * 100).toNumStr()}%"),
                D.Text(actionValue3.toNumStr())
            )
        )
    } else {
        val abnormal = D.Format(
            R.string.action_abnormal_target1_content2_time3,
            arrayOf(
                getTarget(depend),
                getAbnormalContent(actionDetail1),
                D.Text(actionValue3.toNumStr())
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
