package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDurationExtension(): D {
    return if (actionDetail1 == 8) {
        var abnormal: D? = null
        arrayOf(actionValue4, actionValue5, actionValue6, actionValue7).forEach { value ->
            if (value > -1.0) {
                abnormal = if (abnormal == null) {
                    getAbnormalContent(value.toInt())
                } else {
                    abnormal.append(D.Join(arrayOf(
                        D.Format(R.string.comma),
                        getAbnormalContent(value.toInt())
                    )))
                }
            }
        }
        if (abnormal == null) {
            getUnknown()
        } else {
            D.Format(
                R.string.action_duration_extension_target1_content2_value3,
                arrayOf(
                    getTarget(depend),
                    abnormal,
                    D.Text("${(actionValue2 * 100).toNumStr()}%")
                )
            )
        }
    } else {
        getUnknown()
    }
}
