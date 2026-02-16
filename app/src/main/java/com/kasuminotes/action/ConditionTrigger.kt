package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getConditionTrigger(): D {
    return when (actionDetail1) {
        //クルル
        1 -> D.Format(
            R.string.action_branch_status_up_target1_max2,
            arrayOf(getTarget(depend), D.Text(actionValue3.toNumStr()))
        )
        //ヤマト
        2 -> D.Format(R.string.action_branch_attack_critical_target1, arrayOf(getTarget(depend)))
        else -> D.Format(R.string.action_branch_unknown)
    }
}
