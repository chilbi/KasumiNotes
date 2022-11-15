package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDispel(): D {
    val content = when (actionDetail1) {
        3 -> D.Format(R.string.content_status_up_effect)
        10 -> D.Format(R.string.content_barrier)
        else -> D.Unknown
    }

    return D.Format(
        R.string.action_dispel_odds1_target2_content3,
        arrayOf(
            D.Text("${actionValue1.toNumStr()}%"),
            getTarget(depend),
            content
        )
    )
}
