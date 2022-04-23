package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getCutDamage(): D {
    val content = D.Format(
        if (actionDetail1 == 2) R.string.received_magic_damage
        else R.string.received_physical_damage
    )

    return D.Format(
        R.string.action_debuff_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content,
            D.Text("${actionValue1.toNumStr()}%"),
            D.Text(actionValue3.toNumStr())
        )
    )
}
