package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTrigger(): D {
    return when (actionDetail1) {
        //1 ->
        2 -> D.Format(R.string.action_trigger_received_damage_target1, arrayOf(getTarget(depend))).style(bold = true)
        3 -> D.Format(
            R.string.action_trigger_hp_ratio_target1_formula2,
            arrayOf(
                getTarget(depend),
                D.Text("${actionValue3.toNumStr()}%").style(primary = true, bold = true)
            )
        ).style(bold = true)
        4 -> D.Format(R.string.action_trigger_dead).style(bold = true)
        5 -> D.Format(
            R.string.action_trigger_received_critical_damage_target1,
            arrayOf(getTarget(depend))
        ).style(bold = true)
        //6 ->
        7 -> (if (actionValue3 == 90.0) {
            D.Format(R.string.action_trigger_battle_beginning)
        } else {
            D.Format(
                R.string.action_trigger_battle_remaining_time1,
                arrayOf(D.Text(actionValue3.toNumStr()).style(primary = true, bold = true))
            )
        }).style(bold = true)
        8 -> D.Format(R.string.action_trigger_stealth).style(bold = true)
        9 -> D.Format(R.string.action_trigger_break).style(bold = true)
        10 -> D.Format(R.string.action_trigger_received_abnormal_damage_target1)
        11 -> D.Format(R.string.action_trigger_all_break).style(bold = true)
        //12 ->
        13 -> D.Format(
            R.string.action_trigger_hp_target1_formula2,
            arrayOf(
                getTarget(depend),
                D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
            )
        ).style(bold = true)
        14 -> D.Format(R.string.action_trigger_attacked_target1, arrayOf(getTarget(depend))).style(bold = true)
        15 -> D.Format(R.string.action_trigger_abnormal_damage_state_target1, arrayOf(getTarget(depend))).style(bold = true)
        else -> getUnknown()
    }
}
