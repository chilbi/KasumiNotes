package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDarkness(): D {
    return D.Join(
        arrayOf(
            D.Format(
                R.string.action_abnormal_target1_content2_time3,
                arrayOf(
                    getTarget(depend),
                    D.Format(R.string.darkness).style(underline = true),
                    D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
                )
            ),
            D.Format(
                R.string.action_darkness_formula1,
                arrayOf(D.Text("${actionDetail1}%").style(primary = true, bold = true))
            )
        )
    )
}
