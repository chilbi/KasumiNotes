package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDotDamageUp(): D {
    val content = if (actionValue3 == -1.0) {
        D.Format(R.string.content_dot)
    } else {
        var dot: D? = null
        arrayOf(actionValue3, actionValue4, actionValue5, actionValue6).forEach { value ->
            if (value > -1.0) {
                dot = if (dot == null) {
                    getAbnormalDamageContent(value.toInt())
                } else {
                    dot!!.append(D.Join(arrayOf(
                        D.Format(R.string.comma),
                        getAbnormalDamageContent(value.toInt())
                    )))
                }
            }
        }
        if (dot != null) dot!! else D.Format(R.string.content_dot)
    }
    return D.Format(
        R.string.action_dot_damage_up_target1_content2_formula3_max4_time5,
        arrayOf(
            getTarget(depend),
            content,
            D.Text("${actionValue1.toNumStr()}%").style(primary = true, bold = true),
            D.Text(actionValue7.toNumStr()).style(primary = true, bold = true),
            D.Text(actionValue2.toNumStr()).style(primary = true, bold = true)
        )
    )
}
