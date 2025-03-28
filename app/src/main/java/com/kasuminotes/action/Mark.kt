package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getMark(): D {
    val content = D.Format(
        if (actionType == 60 || actionType == 101) {
            if (actionDetail1 == 4) {//チエル
                R.string.content_attack_critical_target1
            } else {
                if (actionDetail3 == 0) {//クロエ、クロエ（ウィンター）、シノブ（パイレーツ）、キョウカ（サマー）
                    R.string.content_attack_hit_enemy_target1
                } else {//ルナ、ルカ（ニューイヤー）、チエル（聖学祭）
                    R.string.content_attack_hit_target1
                }
            }
        } else {//actionType == 77
            when (actionDetail1) {
                1 -> R.string.content_status_up_target1//アキノ（クリスマス）、ユキ（儀装束）、アン＆グレア、ヴルム
                2 -> R.string.content_damage_received_target1//ゴブリンメイスター
                3 -> R.string.content_status_down_target1//ホマレ（サマー）
                4 -> R.string.content_ub_counter_target1//サレン（ニューイヤー）
                else -> R.string.action_branch_unknown
            }
        },
        arrayOf(getTarget(depend))
    )

    val count = if (actionType == 77) actionValue7.toInt() else actionDetail2

    return if (count > 0) {
        D.Format(
            R.string.action_mark_content1_state2_add3_max4_time5,
            arrayOf(
                content,
                getMarkContent(actionValue2.toInt()),
                D.Text(count.toString()).style(primary = true, bold = true),
                D.Text(actionValue1.toNumStr()).style(primary = true, bold = true),
                D.Text(actionValue3.toNumStr()).style(primary = true, bold = true)
            )
        )
    } else {
        D.Format(
            R.string.action_mark_content1_state2_consume3,
            arrayOf(
                content,
                getMarkContent(actionValue2.toInt()),
                D.Text((-count).toString()).style(primary = true, bold = true)
            )
        )
    }
}
