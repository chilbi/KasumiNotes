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
                1 -> R.string.content_status_up_target1//アキノ（クリスマス）、ユキ（儀装束）、アン＆グレア
                3 -> R.string.content_status_down_target1//ホマレ（サマー）
                else -> R.string.content_damage_received_target1//2, ゴブリンメイスター
            }
        },
        arrayOf(getTarget(depend))
    )

    return D.Format(
        R.string.action_mark_content1_state2_add3_max4_time5,
        arrayOf(
            content,
            getStateContent(actionValue2.toInt(), actionId),
            D.Text(actionDetail2.toString()),
            D.Text(actionValue1.toNumStr()),
            D.Text(actionValue3.toNumStr())
        )
    )
}
