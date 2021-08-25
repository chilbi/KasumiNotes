package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getPosture(): D {
    return D.Format(
        R.string.action_posture_time1_rate2,
        arrayOf(
            D.Text(actionValue3.toNumStr()),
            D.Text(actionValue1.toNumStr())
        )
    )
}
