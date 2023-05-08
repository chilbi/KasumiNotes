package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAkinesiaInvalid(): D {
    return if (actionValue1 > 0.0) {
        D.Format(
            R.string.action_akinesia_invalid_target1_count2_time3,
            arrayOf(
                getTarget(depend),
                D.Text(actionValue1.toNumStr()),
                D.Text(actionValue3.toNumStr())
            )
        )
    } else {
        D.Format(
            R.string.action_akinesia_invalid_target1_time2,
            arrayOf(
                getTarget(depend),
                D.Text(actionValue3.toNumStr())
            )
        )
    }
}
