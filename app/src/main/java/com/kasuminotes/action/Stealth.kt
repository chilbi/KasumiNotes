package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getStealth(): D {
    return D.Format(
        R.string.action_stealth_time1,
        arrayOf(D.Text(actionValue1.toNumStr()).style(primary = true, bold = true))
    )
}
