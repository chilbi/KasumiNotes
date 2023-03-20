package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSustainDamage(): D {
    val sustainTime = D.Format(
        R.string.action_sustain_time1,
        arrayOf(D.Text(actionValue3.toNumStr()))
    )
    return if (actionValue1 == 0.0) {
        sustainTime.append(D.Format(R.string.full_stop))
    } else {
        sustainTime.append(
            D.Format(
                R.string.action_sustain_damage_rate1,
                arrayOf(D.Text(actionValue1.toNumStr()))
            )
        )
    }
}
