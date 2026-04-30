package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalField(skillLevel: Int, actions: List<SkillAction>): D {
    val modifyAction = actions.find { it.actionId == actionDetail1 }!!

    val content = when (modifyAction.actionType) {
        8 -> {
            if (modifyAction.actionDetail1 == 1 || modifyAction.actionDetail1 == 2) {
                D.Format(
                    R.string.content_speed_target1_formula2,
                    arrayOf(
                        getAssignment(),
                        D.Text("${(modifyAction.actionValue1 * 100).toNumStr()}%").style(primary = true, bold = true)
                    )
                )
            } else {
                D.Format(
                    R.string.content_state_target1_abnormal2,
                    arrayOf(
                        getAssignment(),
                        getAbnormalContent(modifyAction.actionDetail1).style(underline = true)
                    )
                )
            }
        }
        9 -> {
            val target = getAssignment()
            val content = getAbnormalDamageContent(modifyAction.actionDetail1).style(underline = true)
            val formula = getBaseLvFormula(modifyAction.actionValue1, modifyAction.actionValue2, skillLevel)
            when (modifyAction.actionDetail1) {
                5 -> {
                    val percent = D.Text("${modifyAction.actionValue5.toNumStr()}%").style(primary = true, bold = true)
                    D.Format(
                        R.string.content_state_target1_abnormal_damage2_formula3_percent4,
                        arrayOf(target, content, formula, percent)
                    )
                }
                11 -> {
                    val percent = formula.append(D.Text("%").style(primary = true, bold = true))
                    val max = D.Text(modifyAction.actionValue5.toNumStr()).style(primary = true, bold = true)
                    D.Format(
                        R.string.content_state_target1_abnormal_damage2_formula3_max4,
                        arrayOf(target, content, percent, max)
                    )
                }
                else -> {
                    D.Format(
                        R.string.content_state_target1_abnormal_damage2_formula3,
                        arrayOf(target, content, formula)
                    )
                }
            }
        }
        79 -> {
            D.Format(
                R.string.content_poison_damage_formula1,
                arrayOf(getBaseLvFormula(modifyAction.actionValue1, modifyAction.actionValue2, skillLevel))
            )
        }
        else -> D.Unknown
    }

    val desc = D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue3.toNumStr()),
            content,
            getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        )
    )

    return if (modifyAction.actionType == 9) {
        if (modifyAction.actionDetail3 > 0) {
            desc.append(getInjuredEnergy(modifyAction.actionDetail3))
        } else if (actionDetail3 > 0) {
            desc.append(getInjuredEnergy(actionDetail3))
        } else {
            desc
        }
    } else {
        desc
    }
}
