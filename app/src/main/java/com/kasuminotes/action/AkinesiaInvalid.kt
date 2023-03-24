package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAkinesiaInvalid(): D {
    return D.Format(
        R.string.action_akinesia_invalid_target1_count2_time3,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue1.toNumStr()),
            D.Text(actionValue3.toNumStr())
        )
    )
}
