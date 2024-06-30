package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getRatioDamage(skillLevel: Int, target: D = getTarget(depend)): D {
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

    val desc = D.Format(
        R.string.action_damage_target1_formula2_content3,
        arrayOf(target, formula, getDamageType(actionDetail2))
    )
    return if (actionValue3 == 0.0) desc
    else desc.append(D.Format(R.string.action_max_ratio_damage1, arrayOf(D.Text(actionValue3.toNumStr()))))
}
