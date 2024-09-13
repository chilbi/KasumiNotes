package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEnergyCut(): D {
    return D.Format(
        R.string.action_received_down_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            D.Format(R.string.energy_down),
            D.Text("${(100 - actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}
