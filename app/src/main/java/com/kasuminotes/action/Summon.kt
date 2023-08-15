package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getSummon(): D {
    val target = getTarget(depend)

    val content = when (actionDetail2) {
        403101, 403103 -> D.Format(R.string.summon_403101)//シノブ
        403102 -> D.Format(R.string.summon_403102)//シノブ★六
        404201, 404205 -> D.Format(R.string.summon_404201)//チカ
        407001 -> D.Format(R.string.summon_407001)//ネネカ
        407701 -> D.Format(R.string.summon_407701)//スズメ（サマー）
        408401 -> D.Format(R.string.summon_408401)//チカ（クリスマス）
        408402 -> D.Format(R.string.summon_408402)//チカ（クリスマス）
        408403 -> D.Format(R.string.summon_408403)//チカ（クリスマス）
        418101 -> D.Format(R.string.summon_418101)//ランファ
        425801 -> D.Format(R.string.summon_425801)
        425802 -> D.Format(R.string.summon_425802)
        426201 -> D.Format(R.string.summon_426201)
        426202 -> D.Format(R.string.summon_426202)
        427101 -> D.Format(R.string.summon_427101)
        else -> D.Format(R.string.summon_id1, arrayOf(D.Text(actionDetail2.toString())))
    }

    return if (actionValue7 == 0.0) {
        D.Format(
            R.string.action_summon_target1_content2,
            arrayOf(target, content)
        )
    } else {
        val resId: Int
        val distance: Double

        if (actionValue7 > 0) {
            resId = R.string.action_summon_target1_front_distance2_content3
            distance = actionValue7
        } else {
            resId = R.string.action_summon_target1_back_distance2_content3
            distance = -actionValue7
        }

        D.Format(resId, arrayOf(target, D.Text(distance.toNumStr()), content))
    }
}
