package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDamageCut(skillLevel: Int): D {
    val content = D.Format(
        when (actionDetail1) {
            1 -> R.string.physical_damage
            2 -> R.string.magic_damage
            3 -> R.string.physical_or_magic_damage
            else -> R.string.damage
        }
    )

    val formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        .append(D.Text("%").style(primary = true, bold = true))

    return D.Format(
        R.string.action_received_down_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content.style(underline = true),
            formula,
            D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
        )
    )
}
