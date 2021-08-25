package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFear(): D {
    return D.Format(
        R.string.action_abnormal_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            D.Format(R.string.fear),
            D.Text(actionValue1.toNumStr())
        )
    )
}
