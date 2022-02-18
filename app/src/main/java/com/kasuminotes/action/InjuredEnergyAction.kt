package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getInjuredEnergy(): D {
    return D.Format(R.string.action_injured_energy1, arrayOf(D.Text(actionValue1.toNumStr())))
}
