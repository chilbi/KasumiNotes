package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSpeedOverlay(): D {
    val content = when (actionId) {
        118500203 -> D.Format(R.string.content_strength_seal)
        125000103 -> D.Format(R.string.content_kizuna_certificate)
        else -> D.Text("")
    }
    return content.append(D.Format(
        R.string.action_speed_overlay_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${(actionValue1 * 100).toNumStr()}%"),
            D.Text(actionValue3.toNumStr())
        )
    ))
}
