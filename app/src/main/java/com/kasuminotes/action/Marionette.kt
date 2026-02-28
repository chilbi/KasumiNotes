package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getMarionette(): D {
    val target = getTarget(depend)
    return if (actionValue3 > 0.0) {
        D.Format(
            R.string.action_marionette_target1_time2,
            arrayOf(
                target,
                D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
            )
        )
    } else {
        D.Format(R.string.action_marionette_suspend_target1, arrayOf(target))
    }
}
