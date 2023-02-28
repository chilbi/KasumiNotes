package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageAttenuation(): D {
    return D.Format(
        R.string.action_damage_attenuation_target1_limit2_coefficient3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue5.toNumStr()),
            D.Text(actionValue1.toNumStr()),
            D.Text(actionValue3.toNumStr())
        )
    )
}
