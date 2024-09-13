package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEnvironment(): D {
    return D.Format(
        R.string.action_environment_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            getStateContent(actionDetail2, actionId),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
