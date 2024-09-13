package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRevival(): D {
    return D.Format(
        R.string.action_revival_target1_hp2,
        arrayOf(
            getTarget(depend),
            D.Text("${(actionValue2 * 100).toNumStr()}%").style(primary = true, bold = true)
        )
    )
}
