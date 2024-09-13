package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSpeedField(): D {
    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue5.toNumStr()),
            D.Format(
                R.string.content_speed_target1_formula2,
                arrayOf(
                    getAssignment(),
                    D.Text("${(actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true)
                )
            ),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
