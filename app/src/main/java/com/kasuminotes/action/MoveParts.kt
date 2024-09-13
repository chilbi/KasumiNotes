package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getMoveParts(): D {
    return D.Format(
        R.string.action_move_part1_distance2,
        arrayOf(
            D.Text(actionValue4.toNumStr()),
            D.Text((-actionValue1).toNumStr()).style(primary = true, bold = true)
        )
    )
}
