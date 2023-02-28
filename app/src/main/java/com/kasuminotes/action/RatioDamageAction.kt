package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRatioDamage(skillLevel: Int): D {
    return D.Format(
        if (actionDetail1 == 1) R.string.action_damage_target1_max_ratio2
        else R.string.action_damage_target1_ratio2,
        arrayOf(
            getTarget(depend),
            D.Join(
                arrayOf(
                    getBaseLvFormula(actionValue1, actionValue2, skillLevel) { it },
                    D.Text("%")
                )
            )
        )
    )
}
