package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAkinesiaInvalid(): D {
    val target = getTarget(depend)
    val time = D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
    return if (actionValue1 > 0.0) {
        val count = D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        D.Format(
            R.string.action_akinesia_invalid_target1_count2_time3,
            arrayOf(target, count, time)
        )
    } else {
        D.Format(
            R.string.action_akinesia_invalid_target1_time2,
            arrayOf(target, time)
        )
    }
}
