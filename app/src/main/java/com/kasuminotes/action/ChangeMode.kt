package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeMode(): D {
    val pattern = D.Text((actionDetail2 % 10).toString())
    return when (actionDetail1) {
        1 -> D.Format(
            R.string.action_mode_pattern1_time2,
            arrayOf(
                pattern,
                D.Text(actionValue1.toNumStr())
            )
        )
        2 -> D.Format(
            R.string.action_mode_pattern1_energy2,
            arrayOf(
                pattern,
                D.Text(actionValue1.toNumStr())
            )
        )
        3 -> D.Format(
            R.string.action_mode_return_pattern1,
            arrayOf(pattern)
        )
        else -> getUnknown()
    }
}
