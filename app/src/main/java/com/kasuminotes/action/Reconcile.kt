package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getReconcile(): D {
    return D.Format(
        R.string.action_reconcile_target1_time2,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
