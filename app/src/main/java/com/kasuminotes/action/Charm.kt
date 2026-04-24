package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getCharm(): D {
    val content = getCharmContent(actionDetail1).style(underline = true)
    return D.Format(
        R.string.action_abnormal_target1_content2_time3,
        arrayOf(
            getTarget(depend),
            content,
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun getCharmContent(detail: Int): D {
    return D.Format(when (detail) {
        1 -> R.string.chaos
        3 -> R.string.cannot_invalidated_fetter
        else -> R.string.charm//0
    })
}
