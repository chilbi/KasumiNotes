package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getInjuredEnergy(): D {
    return if (actionValue1 == 0.0) {
        D.Format(R.string.action_no_injured_energy)
    } else {
        D.Format(
            R.string.action_injured_energy1,
            arrayOf(D.Text("${(actionValue1 * 100).toNumStr()}%"))
        )
    }
}
