package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getAbnormalDamage(skillLevel: Int): D {
    val content = when (actionDetail1) {
        1 -> D.Format(R.string.poison)
        2 -> D.Format(R.string.burn)
        3 -> D.Format(R.string.curse)
        4 -> D.Format(R.string.fierce_poison)
        else -> D.Unknown
    }

    return D.Format(
        R.string.action_abnormal_target1_content2_formula3_time4,
        arrayOf(
            getTarget(depend),
            content,
            getBaseLvFormula(actionValue1, actionValue2, skillLevel),
            D.Text(actionValue3.toNumStr())
        )
    )
}
