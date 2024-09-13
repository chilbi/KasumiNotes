package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDispel(): D {
    val content = when (actionDetail1) {
        1, 3 -> D.Format(R.string.content_status_up_effect)
        2 -> D.Format(R.string.content_status_down_effect)
        10 -> D.Format(R.string.content_barrier)
        else -> D.Unknown
    }

    val result = D.Format(
        R.string.action_dispel_target1_content2,
        arrayOf(
            getTarget(depend),
            content.style(underline = true)
        )
    )

    return if (actionValue1 < 100.0) {
        result.insert(
            D.Format(
                R.string.action_odds1,
                arrayOf(D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true))
            )
        )
    } else {
        result
    }
}
