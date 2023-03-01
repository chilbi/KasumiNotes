package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSilence(): D {
    return D.Format(
        R.string.action_state_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            D.Format(R.string.silence),
            D.Text(actionValue1.toNumStr())
        )
    )
}
