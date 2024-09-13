package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSkillCounter(): D {
    return D.Format(
        R.string.action_skill_counter1_max2,
        arrayOf(
            D.Format(
                R.string.counter_num1,
                arrayOf(D.Text(actionDetail1.toString()))
            ),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
