package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalDamageExtension(): D {
    return D.Format(
        R.string.action_abnormal_damage_extension_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}
