package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.roundToInt

fun SkillAction.getStatus(skillLevel: Int): D {
    val value1: Double
    val isBuff: Boolean
    if (actionDetail1 == 141) {
        value1 = 2.0
        isBuff = true
    } else{
        value1 = actionValue1
        isBuff = actionDetail1 % 10 == 0
    }

    val formula = when (value1) {
        1.0 -> getBaseLvFormula(actionValue2, actionValue3, skillLevel)
        2.0 -> D.Text("${actionValue2.roundToInt()}%")
        else -> D.Unknown
    }

    return D.Format(
        if (isBuff) R.string.action_buff_target1_content2_formula3_time4
        else R.string.action_debuff_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            getStatusContent(actionDetail1 / 10),
            formula,
            D.Text(actionValue4.toNumStr())
        )
    )
}
