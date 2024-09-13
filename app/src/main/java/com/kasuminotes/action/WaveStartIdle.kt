package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getWaveStartIdle(): D {
    return D.Format(
        R.string.action_wave_start_idle_time1,
        arrayOf(D.Text(actionValue1.toNumStr()).style(primary = true, bold = true))
    )
}
