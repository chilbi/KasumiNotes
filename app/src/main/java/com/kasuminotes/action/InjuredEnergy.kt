package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getInjuredEnergy(): D {
    return getInjuredEnergy(100 - (actionValue1 * 100).toInt())
//    return if (actionValue1 == 0.0) {
//        D.Format(R.string.action_no_injured_energy)
//    } else {
//        D.Format(
//            R.string.action_injured_energy1,
//            arrayOf(D.Text("${(actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true))
//        )
//    }
}

fun SkillAction.appendInjuredEnergy(originDesc: D): D {
    return if (actionDetail3 > 0) {
        originDesc.append(getInjuredEnergy(actionDetail3))
    } else {
        originDesc
    }
}

fun getInjuredEnergy(detail: Int): D {
    return if (detail >= 100) {
        D.Format(R.string.action_no_injured_energy)
    } else {
        D.Format(
            R.string.action_injured_energy1,
            arrayOf(D.Text("${100 - detail}%").style(primary = true, bold = true))
        )
    }
}
