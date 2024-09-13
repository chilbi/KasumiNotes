package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getPassiveDamageUp(): D {
    val target = getTarget(depend)
    return D.Format(
        R.string.action_passive_damage_up_target1_formula2_max3_time4,
        arrayOf(
            target,
            D.Format(R.string.formula_m1_m2, arrayOf(
                D.Text("${(actionValue1 * 100).toNumStr()}%"),
                D.Format(R.string.count_of_abnormal_state_and_status_down_effect_target1, arrayOf(target))
            )).style(primary = true),
            D.Text("${(actionValue2 * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
