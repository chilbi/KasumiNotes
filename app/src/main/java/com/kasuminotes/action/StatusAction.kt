package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillAction
import kotlin.math.roundToInt

fun SkillAction.getStatus(skillLevel: Int, property: Property?): D {
    var value1 = actionValue1
    var isBuff = actionDetail1 % 10 == 0
    if (actionDetail1 >= 140) {
        value1 = 2.0//百分比
        isBuff = !isBuff
    }
    if (actionDetail1 in 110..129) {
        value1 = 2.0
    }

    val formula = if (value1 == 1.0) {
        getBaseLvFormula(actionValue2, actionValue3, skillLevel)
    } else {
        var str = "${actionValue2.roundToInt()}%"
        if (property != null) {
            str += if (isBuff) "(+" else "(-"
            str += "${(property[getStatusIndex(actionDetail1 / 10)] * actionValue2 / 100).roundToInt()})"
        }
        val percent = D.Text(str)
        if (actionDetail1 < 50) D.Format(R.string.content_initial_value1, arrayOf(percent))
        else percent
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
