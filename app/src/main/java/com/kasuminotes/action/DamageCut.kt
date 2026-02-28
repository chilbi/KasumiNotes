package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getDamageCut(skillLevel: Int): D {
    var value1 = actionValue1
    var value2 = actionValue2
    val content = getDamageCutContent()
    if (actionDetail1 == 4 || actionDetail1 == 5) {
        value1 /= 100
        value2 /= 100
    }

    val formula = getBaseLvFormula(value1, value2, skillLevel)
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

fun SkillAction.getDamageCutContent(): D {
    return D.Format(
        when (actionDetail1) {
            1, 4 -> R.string.physical_damage
            2, 5 -> R.string.magic_damage
            3 -> R.string.physical_or_magic_damage
            else -> R.string.damage
        }
    )
}

fun SkillAction.getDamageCutEffect(skillLevel: Int): SkillEffect {
    var value1 = actionValue1
    var value2 = actionValue2
    val content = getDamageCutContent()
    if (actionDetail1 == 4 || actionDetail1 == 5) {
        value1 /= 100
        value2 /= 100
    }
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_damage_cut_content1, arrayOf(content)),
        D.Text("${(value1 + value2 * skillLevel).toNumStr()}%"),
        actionValue3,
        0.5f,
        SkillEffect.damageCut
    )
}
