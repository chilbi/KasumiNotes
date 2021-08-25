package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTrigger(): D {
    return when (actionDetail1) {
        3 ->D.Format(
            R.string.action_trigger_hp_target1_formula2,
            arrayOf(
                getTarget(depend),
                D.Text("${actionValue3.toNumStr()}%")
            )
        )
        else -> getUnknown()
    }
}
