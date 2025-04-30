package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getConvertToDot(): D {
    return D.Format(
        R.string.action_convert_to_dot_target1_formula2_duration3_time4,
        arrayOf(
            getTarget(depend),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}
