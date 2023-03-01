package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRatioDamage(skillLevel: Int): D {
    val formula = D.Format(
        if (actionDetail1 == 1) R.string.content_max_hp_ratio1
        else R.string.content_hp_ratio1,
        arrayOf(
            D.Join(
                arrayOf(
                    getBaseLvFormula(actionValue1, actionValue2, skillLevel) { it },
                    D.Text("%")
                )
            )
        )
    )

    return D.Format(
        R.string.action_damage_target1_formula2_content3,
        arrayOf( getTarget(depend), formula, getDamageType(actionDetail2))
    )
}
