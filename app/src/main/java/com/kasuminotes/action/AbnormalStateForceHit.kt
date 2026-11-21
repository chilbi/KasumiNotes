package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalStateForceHit(): D {
    val state: D = if (actionDetail1 in listOf(8, 9, 11, 12, 13)) {
        val getContent: (Int) -> D = when (actionDetail1) {
            8 -> ::getAbnormalContent
            9 -> ::getAbnormalDamageContent
            11 -> ::getCharmContent
            12 -> ::getDarknessContent
            else -> ::getSilenceContent
        }
        getContent(actionValue4.toInt())
    } else {
        D.Text("???")
    }
    return D.Format(
        R.string.action_abnormal_state_force_hit_target1_state2,
        arrayOf(getTarget(depend), state.style(underline = true))
    )
}
