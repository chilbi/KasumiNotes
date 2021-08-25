package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSustain(skillLevel: Int): D {
    val formula = when (actionValue1) {
        1.0 -> getBaseLvFormula(actionValue2, actionValue3, skillLevel)
        2.0 -> D.Text("${actionValue2.toNumStr()}%")
        else -> D.Unknown
    }

    return D.Format(
        if (actionDetail1 % 10 == 0) R.string.action_sustain_buff_time1_target2_content3_formula4_count5
        else R.string.action_sustain_debuff_time1_target2_content3_formula4_count5,
        arrayOf(
            D.Text(actionValue4.toNumStr()),
            getTarget(depend),
            getStatusContent(actionDetail1 / 10),
            formula,
            D.Text(actionDetail3.toString())
        )
    )
}
