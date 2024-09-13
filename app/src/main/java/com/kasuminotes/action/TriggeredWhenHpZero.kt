package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTriggeredWhenHpZero(): D {
    val desc = D.Format(
        R.string.action_triggered_when_hp_zero_target1,
        arrayOf(getTarget(depend))
    )
    return if (actionValue1 > 0.0) {
        desc.append(
            D.Format(
                R.string.action_trigger_count1,
                arrayOf(D.Text(actionValue1.toNumStr()))
            )
        )
    } else {
        desc
    }
}
