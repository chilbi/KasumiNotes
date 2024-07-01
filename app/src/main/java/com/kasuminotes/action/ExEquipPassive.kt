package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getExEquipPassive(): D {
    return when (actionDetail1) {
        7 -> {
            D.Format(
                R.string.action_passive_battle_remaining_time1,
                arrayOf(D.Text(actionValue3.toNumStr()))
            )
        }
        else -> D.Format(R.string.action_passive_battle_beginning)
    }
}
