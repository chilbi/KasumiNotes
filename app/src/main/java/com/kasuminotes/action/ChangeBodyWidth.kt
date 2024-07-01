package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getChangeBodyWidth(): D {
    return D.Format(
        if (actionDetail2 > 0) R.string.action_change_body_width1_target2_num3
        else R.string.action_change_body_width1_target2,
        arrayOf(
            D.Text(actionValue1.toNumStr()),
            getTarget(depend),
            D.Text(actionDetail2.toString())
        )
    )
}
