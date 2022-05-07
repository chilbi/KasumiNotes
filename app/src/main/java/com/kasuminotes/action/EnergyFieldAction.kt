package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getEnergyField(skillLevel: Int): D {
    return D.Format(
        R.string.action_field_target1_range2_content3_time4,
        arrayOf(
            getTarget(depend),
            D.Text(actionValue5.toNumStr()),
            D.Format(
                R.string.content_regeneration_content1_formula2,
                arrayOf(
                    D.Format(R.string.energy),
                    getBaseLvFormula(actionValue1, actionValue2, skillLevel)
                )
            ),
            D.Text(actionValue3.toNumStr())
        )
    )
}
