package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSummon(): D {
    val target = getTarget(depend)

    val content = when (actionDetail2) {
        403101 -> D.Format(R.string.summon_403101)//シノブ
        404201, 404205 -> D.Format(R.string.summon_404201)//チカ
        407001 -> D.Format(R.string.summon_407001)//ネネカ
        407701 -> D.Format(R.string.summon_407701)//スズメ（サマー）
        408401 -> D.Format(R.string.summon_408401)//チカ（クリスマス）
        408402 -> D.Format(R.string.summon_408402)//チカ（クリスマス）
        408403 -> D.Format(R.string.summon_408403)//チカ（クリスマス）
        418101 -> D.Format(R.string.summon_418101)//ランファ
        else -> D.Text(description)
    }

    return if (actionValue7 == 0.0) {
        D.Format(
            R.string.action_summon_target1_content2,
            arrayOf(target, content)
        )
    } else {
        D.Format(
            R.string.action_summon_target1_distance2_content3,
            arrayOf(
                target,
                D.Text(actionValue7.toNumStr()),
                content
            )
        )
    }
}
