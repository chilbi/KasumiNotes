package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getHiding(): D  {
    return D.Format(
        R.string.action_hiding_target1_time2,
        arrayOf(getTarget(depend), D.Text(actionValue1.toNumStr()))
    )
}
