package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFear(): D {
    val state = D.Format(
        if (actionDetail1 == 1) R.string.deactivate else R.string.fear
    ).style(underline = true)
    val result = D.Format(
        R.string.action_abnormal_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            state,
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
    return if (actionDetail1 == 1) {
        result.append(D.Format(R.string.action_deactivate1, arrayOf(state)))
    } else {
        result
    }
}
