package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getFixedDamage(): D {
    val target = getTarget(depend)
    val formula = D.Text("${actionValue1.toNumStr()}%")
    val time = D.Text(actionValue3.toNumStr())
    return if (actionValue5 != 0.0) {
        D.Format(
            R.string.action_fixed_damage_target1_formula2_max3_time4,
            arrayOf(target, formula, D.Text(actionValue5.toNumStr()), time)
        )
    } else {
        D.Format(
            R.string.action_fixed_damage_target1_formula2_time3,
            arrayOf(target, formula, time)
        )
    }
}
