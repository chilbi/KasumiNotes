package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTrigger(): D {
    return when (actionDetail1) {
        3 -> {
            D.Format(
                R.string.action_trigger_hp_target1_formula2,
                arrayOf(
                    getTarget(depend),
                    D.Text("${actionValue3.toNumStr()}%")
                )
            )
        }
        7 -> {
            if (actionValue3 == 90.0) {
                D.Format(R.string.action_trigger_battle_beginning)
            } else {
                D.Format(
                    R.string.action_trigger_battle_began_passed_time1,
                    arrayOf(D.Text(actionValue3.toNumStr()))
                )
            }
        }
        8 -> {
            D.Format(R.string.action_trigger_stealth)
        }
        9 -> {
            D.Format(R.string.action_trigger_break)
        }
        11 -> {
            D.Format(R.string.action_trigger_all_break)
        }
        else -> getUnknown()
    }
}
