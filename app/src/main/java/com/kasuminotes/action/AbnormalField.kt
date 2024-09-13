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
        79 -> {
            D.Format(
                R.string.content_poison_damage_formula1,
                arrayOf(getBaseLvFormula(modifyAction.actionValue1, modifyAction.actionValue2, skillLevel))
            )
        }
        else -> D.Unknown
    }

    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue3.toNumStr()),
            content,
            D.Text(actionValue1.toNumStr()).style(primary = true, bold = true)
        )
    )
}
