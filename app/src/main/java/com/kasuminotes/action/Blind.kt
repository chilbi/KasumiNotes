package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getBlind(): D {
    return if (actionValue1 == 2.0) {
        D.Format(
            R.string.action_blind_target1_count2,
            arrayOf(
                getTarget(depend),
                D.Text(actionValue2.toNumStr())
            )
        )
    } else {
        getUnknown()
    }
}
