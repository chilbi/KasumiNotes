package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getInjuredEnergyMark(): D {
    return D.Format(
        R.string.action_injured_energy1_mark2_add3_max4_time5_target6,
        arrayOf(
            D.Text(actionValue1.toNumStr()),
            getStateContent(actionDetail2),
            D.Text(actionValue3.toNumStr()),
            D.Text(actionValue4.toNumStr()),
            D.Text(actionValue5.toNumStr()),
            getTarget(depend)
        )
    )
}
