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
            ).style(primary = true)
        }
        4 -> D.Format(R.string.action_trigger_dead).style(primary = true)
        5 -> D.Format(R.string.action_trigger_received_critical_damage_target1, arrayOf(getTarget(depend))).style(primary = true)
        7 -> {
            if (actionValue3 == 90.0) {
                D.Format(R.string.action_trigger_battle_beginning).style(primary = true)
            } else {
                D.Format(
                    R.string.action_trigger_battle_remaining_time1,
                    arrayOf(D.Text(actionValue3.toNumStr()).style(primary = true, bold = true))
                ).style(primary = true)
            }
        }
        8 -> D.Format(R.string.action_trigger_stealth).style(primary = true)
        9 -> D.Format(R.string.action_trigger_break).style(primary = true)
        11 -> D.Format(R.string.action_trigger_all_break).style(primary = true)
        14 -> D.Format(R.string.action_trigger_attacked_target1, arrayOf(getTarget(depend))).style(primary = true)
        15 -> D.Format(R.string.action_trigger_dot_target1, arrayOf(getTarget(depend))).style(primary = true)
        else -> getUnknown()
    }
}
