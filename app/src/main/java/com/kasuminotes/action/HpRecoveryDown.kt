package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect

fun SkillAction.getHpRecoveryDown(skillLevel: Int): D {
    return D.Format(
        R.string.action_hp_recovery_down_target1_formula2_time3,
        arrayOf(
            getTarget(depend),
            D.Text("${((actionValue1 + actionValue4 * skillLevel) * 100).toNumStr()}%").style(primary = true, bold = true),
            D.Text((if (actionValue2 == 0.0) actionValue3 else actionValue2).toNumStr()).style(primary = true, bold = true)
        )
    )
}

fun SkillAction.getHpRecoveryDownEffect(skillLevel: Int): SkillEffect {
    return SkillEffect(
        getTarget(null),
        D.Format(R.string.effect_hp_recovery_down),
        D.Text("${((actionValue1 + actionValue4 * skillLevel) * 100).toNumStr()}%"),
        if (actionValue2 == 0.0) actionValue3 else actionValue2,
        0.5f,
        10
    )
}
