package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getHitCount(): D {
    return D.Format(
        R.string.action_hit_count_target1_time2_count3,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
