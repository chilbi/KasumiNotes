package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSilence(): D {
    return D.Format(
        R.string.action_state_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            getSilenceContent(actionDetail1).style(underline = true),
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun getSilenceContent(detail: Int): D {
    val silenceType = D.Text(if (detail == 1) "UB" else "")
    return silenceType.append(D.Format(R.string.silence))
}
