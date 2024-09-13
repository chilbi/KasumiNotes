package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSpeedOverlay(): D {
    val content = when (actionId) {
        118500203 -> D.Format(R.string.content_strength_seal)
        125000103 -> D.Format(R.string.content_kizuna_certificate)
        126400106 -> D.Format(R.string.content_full_charge)
        126410306 -> D.Format(R.string.content_system_down)
        else -> D.Text("")
    }
    return content.append(D.Format(
        R.string.action_speed_overlay_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            D.Format(if (actionDetail1 == 1) R.string.content_down else R.string.content_up),
            D.Text("${(actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    ))
}
