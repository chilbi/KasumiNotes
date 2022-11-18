package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageCut(): D {
    val content = D.Format(
        when (actionDetail1) {
            1 -> R.string.received_physical_damage
            2 -> R.string.received_magic_damage
            3 -> R.string.received_physical_or_magic_damage
            else -> R.string.received_damage
        }
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
