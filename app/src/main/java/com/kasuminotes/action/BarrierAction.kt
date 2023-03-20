package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getBarrier(skillLevel: Int): D {
    @StringRes
    val resId = when (actionDetail1) {
        1, 2, 5 -> R.string.action_barrier_guard_target1_formula2_content3_time4
        else -> R.string.action_barrier_drain_target1_formula2_content3_time4
    }

    val content = when (actionDetail1) {
        1, 3 -> D.Format(R.string.physical)
        2, 4 -> D.Format(R.string.magic)
        5, 6 -> D.Join(arrayOf(D.Format(R.string.physical), D.Text("/"), D.Format(R.string.magic)))
        else -> D.Unknown
    }

    return D.Format(
        resId,
        arrayOf(
            getTarget(depend),
            getBaseLvFormula(actionValue1, actionValue2, skillLevel),
            content,
            D.Text(actionValue3.toNumStr())
        )
    )
}
