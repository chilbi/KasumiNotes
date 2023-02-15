package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSpeedOverlay(): D {
    val content = when (actionId) {
        118500203 -> D.Format(R.string.content_strength_seal)
        125000103 -> D.Format(R.string.content_kizuna_certificate)
        else ->D.Format(R.string.content_unknown_state)
    }
    return D.Format(
        R.string.action_speed_overlay_content1_target2_formula3_time4,
        arrayOf(
            content,
            getTarget(depend),
            D.Text("${(actionValue1 * 100).toNumStr()}%"),
            D.Text(actionValue3.toNumStr())
        )
    )
}
