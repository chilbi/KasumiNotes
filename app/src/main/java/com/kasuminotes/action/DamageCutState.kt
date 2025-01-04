package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageCutState(): D  {
    return D.Format(
        R.string.action_damage_cut_target1_state2_time3_cut4,
        arrayOf(
            getTarget(depend),
            getMarkContent(actionDetail2),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true)
        )
    )
}
