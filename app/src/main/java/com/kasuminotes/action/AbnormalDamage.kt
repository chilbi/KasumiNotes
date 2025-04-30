package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getAbnormalDamage(skillLevel: Int): D {
    val target = getTarget(depend)
    val content = getAbnormalDamageContent(actionDetail1).style(underline = true)
    val formula = getBaseLvFormula(actionValue1, actionValue2, skillLevel)
    val time = D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)

    val damage = when (actionDetail1) {
        5 -> {
            val percent = D.Text("${actionValue5.toNumStr()}%").style(primary = true, bold = true)
            D.Format(
                R.string.action_abnormal_target1_content2_formula3_percent4_time5,
                arrayOf(target, content, formula, percent, time)
            )
        }
        11 -> {
            val percent = formula.append(D.Text("%").style(primary = true, bold = true))
            val max = D.Text(actionValue5.toNumStr()).style(primary = true, bold = true)
            D.Format(
                R.string.action_abnormal_target1_content2_formula3_max4_time5,
                arrayOf(target, content, percent, max, time)
            )
        }
        else -> {
            D.Format(
                R.string.action_abnormal_target1_content2_formula3_time4,
                arrayOf(target, content, formula, time)
            )
        }
    }

    return appendInjuredEnergy(damage)
}

fun SkillAction.getAbnormalDamageEffect(skillLevel: Int): SkillEffect {
    val label: D = D.Format(
        R.string.effect_abnormal_damage1,
        arrayOf(getAbnormalDamageContent(actionDetail1))
    )
    var value: D = D.Text((actionValue1 + actionValue2 * skillLevel).toNumStr())
    var weight = 0.5f
    if (actionValue5 != 0.0) {
        value = value.append(D.Format(
            R.string.effect_abnormal_damage_up1,
            arrayOf(D.Text("${actionValue5.toNumStr()}%"))
        ))
        weight = 1f
    }
    return SkillEffect(
        getTarget(null),
        label,
        value,
        actionValue3,
        weight,
        SkillEffect.abnormalDamage
    )
}
