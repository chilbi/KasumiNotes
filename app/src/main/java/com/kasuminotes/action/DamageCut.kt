package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageCut(): D {
    val content = D.Format(
        when (actionDetail1) {
            1 -> R.string.physical_damage
            2 -> R.string.magic_damage
            3 -> R.string.physical_or_magic_damage
            else -> R.string.damage
        }
    )

    return D.Format(
        R.string.action_received_down_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content.style(underline = true),
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
