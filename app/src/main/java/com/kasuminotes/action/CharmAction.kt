package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getCharm(): D {
    return D.Format(
        R.string.action_abnormal_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            D.Format(if (actionDetail1 == 0) R.string.charm else R.string.chaos),
            D.Text(actionValue1.toNumStr())
        )
    )
}