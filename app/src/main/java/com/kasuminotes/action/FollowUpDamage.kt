package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFollowUpDamage(): D {
    return D.Format(
        R.string.action_follow_up_damage_target1_percent2_type3_time4,
        arrayOf(
            getTarget(depend),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true),
            getDamageType(actionDetail1),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
