package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getIllusion(): D  {
    return D.Format(
        R.string.action_illusion_target1_time2_cut3,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true)
        )
    )
}
