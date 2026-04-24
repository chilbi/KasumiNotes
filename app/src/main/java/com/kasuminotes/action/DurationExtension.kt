package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getDurationExtension(skillLevel: Int): D {
    return if (actionDetail1 in listOf(8, 9, 11, 12, 13)) {
        val getContent: (Int) -> D = when (actionDetail1) {
            8 -> ::getAbnormalContent
            9 -> ::getAbnormalDamageContent
            11 -> ::getCharmContent
            12 -> ::getDarknessContent
            else -> ::getSilenceContent
        }
        var abnormal: D? = null
        arrayOf(actionValue4, actionValue5, actionValue6, actionValue7).forEach { value ->
            if (value > -1.0) {
                abnormal = abnormal?.append(D.Join(arrayOf(
                    D.Format(R.string.comma),
                    getContent(value.toInt())
                )))
                    ?: getContent(value.toInt())
            }
        }
        if (abnormal == null) {
            getUnknown()
        } else {
            val isPercent = actionValue1 == 1.0
            val value = actionValue2 + actionValue3 * skillLevel
            val resId: Int
            val valueDesc: D
            if (isPercent) {
                resId = R.string.action_duration_extension_target1_content2_percent3
                valueDesc = D.Text("${100 + value}%").style(primary = true, bold = true)
            } else {
                resId = if (value >= 0.0) {
                    R.string.action_duration_extension_target1_content2_increase3
                } else {
                    R.string.action_duration_extension_target1_content2_decrease3
                }
                valueDesc = getBaseLvFormula(actionValue2, actionValue3, skillLevel)
            }
            D.Format(
                resId,
                arrayOf(
                    getTarget(depend),
                    abnormal,
                    valueDesc
                )
            )
        }
    } else {
        getUnknown()
    }
}
