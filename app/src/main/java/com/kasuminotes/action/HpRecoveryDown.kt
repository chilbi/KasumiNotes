package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getHpRecoveryDown(): D {
    return D.Format(
        R.string.action_hp_recovery_down_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${(actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text((if (actionValue2 == 0.0) actionValue3 else actionValue2).toNumStr()).style(primary = true, bold = true)
        )
    )
}
