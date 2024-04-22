package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDotDamageUp(): D {
    val content = if (actionValue1 == 400.0) {
        D.Format(R.string.content_dot)
    } else {//9900.0
        var dot: D? = null
        arrayOf(actionValue3, actionValue4, actionValue5, actionValue6).forEach { value ->
            if (value > -1.0) {
                if (dot == null) {
                    dot = getAbnormalDamageContent(value.toInt())
                } else {
                    dot = dot!!.append(D.Join(arrayOf(
                        D.Format(R.string.comma),
                        getAbnormalDamageContent(value.toInt())
                    )))
                }
            }
        }
        if (dot != null) dot!! else D.Unknown
    }
    return D.Format(
        R.string.action_dot_damage_up_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content,
            D.Text(actionValue7.toNumStr()),
            D.Text(actionValue2.toNumStr())
        )
    )
}
