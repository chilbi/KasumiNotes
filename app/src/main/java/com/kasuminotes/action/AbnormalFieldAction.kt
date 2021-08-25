package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalField(skillLevel: Int, actions: List<SkillAction>): D {
    val modifyAction = actions.find { it.actionId == actionDetail1 }!!

    val content = when (modifyAction.actionType) {
        8 -> {
            if (modifyAction.actionDetail1 == 1 || modifyAction.actionDetail1 == 2) {
                D.Format(
                    R.string.content_speed_formula1,
                    arrayOf(D.Text("${(modifyAction.actionValue1 * 100).toNumStr()}%"))
                )
            } else {
                D.Format(
                    R.string.content_abnormal1,
                    arrayOf(getAbnormalContent(modifyAction.actionDetail1))
                )
            }
        }
        79 -> {
            D.Format(
                R.string.content_poison_damage_by_behaviour_formula1,
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
            D.Text(actionValue1.toNumStr())
        )
    )
}
