package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.roundToInt

fun SkillAction.getStatusField(skillLevel: Int): D {
    val formula = when (actionDetail2) {
        1 -> getBaseLvFormula(actionValue1, actionValue2, skillLevel)
        2 -> D.Text("${actionValue1.roundToInt()}%")
        else -> D.Unknown
    }

    val content = D.Format(
        if (actionDetail1 % 10 == 0) R.string.content_buff_target1_content2_formula3
        else R.string.content_debuff_target1_content2_formula3,
        arrayOf(
            getAssignment(),
            getStatusContent(actionDetail1 / 10),
            formula
        )
    )

    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue5.toNumStr()),
            content,
            D.Text(actionValue3.toNumStr()),
        )
    )
}
