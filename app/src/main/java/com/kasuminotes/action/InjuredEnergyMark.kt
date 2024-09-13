package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getInjuredEnergyMark(): D {
    return D.Format(
        R.string.action_injured_energy1_mark2_add3_max4_time5_target6,
        arrayOf(
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true),
            getStateContent(actionDetail2, actionId),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue4.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue5.toNumStr()).style(primary = true, bold = true),
            getTarget(depend)
        )
    )
}
