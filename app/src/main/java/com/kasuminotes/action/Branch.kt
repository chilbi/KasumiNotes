package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getBranch(): Array<Pair<Int, D>> {
    return when (actionType) {
        23 -> getDependBranch()
        28 -> getNoDependBranch()
        42 -> getCounterBranch()
        53 -> getExistsFieldBranch()
        63 -> getDamageReceivedBranch()
        111 -> getSingleBranch()
        else -> emptyArray()
    }
}

private val SkillAction.finalDepend: SkillAction?
    get() = if (depend == null) this else if (depend!!.actionType == 7) null else depend!!.finalDepend

private fun SkillAction.getDependBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()
    val target = finalDepend?.copy(actionType = 23)?.getTarget(null) ?: getTarget(depend)

    when (actionDetail1) {
        // ホマレ
        in 6001..6999 -> {
            setStateBranch(branch,actionDetail1 - 6000, actionValue3)
        }
        in 4001..4999 -> {
            val id = R.string.action_branch_target1_element2_p3
            val element = getElementType(actionDetail1 - 4000)
            setBranch(
                branch,
                D.Format(id, arrayOf(target, element, D.Format(R.string.action_branch_element_yes).tag(true))),
                D.Format(id, arrayOf(target, element, D.Format(R.string.action_branch_element_no).tag(false)))
            )
        }
        // アメス
        1900 -> {
            val id = R.string.action_branch_barrier_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_barrier_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_barrier_no).tag(false)))
            )
        }
        // ミミ（サマー）
        1800 -> {
            val id = R.string.action_branch_multi_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_multi_target_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_multi_target_no).tag(false)))
            )
        }
        // キャル（オーバーロード）
        1600 -> {
            val state = D.Format(R.string.fear)
            setAbnormalBranch(branch, target, state)
        }
        in 1501..1599 -> {
            val state = getAbnormalContent(actionDetail1 - 1500)
            setAbnormalBranch(branch, target, state)
        }
        // タマキ、ミサト（サマー）
        1300 -> {
            val id = R.string.action_branch_atk_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_physical).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_magic).tag(false)))
            )
        }
        // ぺコリーヌ（プリンセス）、レイ（ハロウィン）
        in 901..999 -> {
            val value = D.Text("${actionDetail1 % 100}%")
            val id = R.string.action_branch_hp_target1_value2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(target, value, D.Format(R.string.action_branch_lt).tag(true))),
                D.Format(id, arrayOf(target, value, D.Format(R.string.action_branch_gt).tag(false)))
            )
        }
        900 -> {
            val id = R.string.action_branch_hp_max_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_hp_max_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_hp_max_no).tag(false)))
            )
        }
        // 天秤座
        710 -> {
            val id = R.string.action_branch_break_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_break_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_break_no).tag(false))),
            )
        }
        // マコト（サマー）
        700 -> {
            val id = R.string.action_branch_single_target_p1
            setBranch(
                branch,
                D.Format(id, arrayOf(D.Format(R.string.action_branch_single_target_yes).tag(true))),
                D.Format(id, arrayOf(D.Format(R.string.action_branch_single_target_no).tag(false)))
            )
        }
        // ルナ、クリスティーナ（クリスマス）
        in 600..699 -> {
            setStateBranch(branch,actionDetail1 - 600, actionValue3)
        }
        // アオイ、アオイ（編入生）、ミツキ（オーエド）
        in 500..599 -> {
            setAbnormalDamageBranch(branch, target)
        }
        // イオ
        300 -> {
            val state = D.Format(R.string.charm)
            setAbnormalBranch(branch, target, state)
        }
        // 射手座
        200 -> {
            val state = D.Format(R.string.darkness)
            setAbnormalBranch(branch, target, state)
        }
        // カヤ（タイムトラベル）
        101 -> {
            val id = R.string.action_branch_speed_up_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_speed_up_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_speed_up_no).tag(false)))
            )
        }
        // レム
        100 -> {
            val id = R.string.action_branch_akinesia_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_akinesia_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_akinesia_no).tag(false)))
            )
        }
    }

    return branch.toTypedArray()
}

private fun SkillAction.getNoDependBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
        // ルカ（ニューイヤー）、シノブ
        in 6001..6999 -> {
            setStateBranch(branch,actionDetail1 - 6000, actionValue3)
        }
        in 4001..4999 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_target1_element2_p3
            val element = getElementType(actionDetail1 - 4000)
            setBranch(
                branch,
                D.Format(id, arrayOf(target, element, D.Format(R.string.action_branch_element_yes).tag(true))),
                D.Format(id, arrayOf(target, element, D.Format(R.string.action_branch_element_no).tag(false)))
            )
        }
        // ライラエル
        in 3001..3999 -> {
            val target = getTarget(depend)
            val state = getMarkContent(actionDetail1 - 3000)
            val id = R.string.action_branch_target1_environment2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_environment_yes).tag(true))),
                D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_environment_no).tag(false)))
            )
        }
        // アキノ（サマー）、ハツネ（ニューイヤー）
        2000, 2001 -> {
            if (targetCount == 1) {
                val target = getTarget(depend)
                val id = R.string.action_branch_atk_target1_p2
                val physical = D.Format(id, arrayOf(target, D.Format(R.string.action_branch_physical).tag(true)))
                val magic = D.Format(id, arrayOf(target, D.Format(R.string.action_branch_magic).tag(false)))
                var detail2 = actionDetail2
                var detail3 = actionDetail3
                if (actionDetail1 == 2001) {
                    detail2 = actionDetail3
                    detail3 = actionDetail2
                }
                if (detail2 != 0) {
                    branch.add(detail2 to physical)
                }
                if (detail3 != 0) {
                    branch.add(detail3 to magic)
                }
            } else {
                val target = this.copy(actionType = -1).getTarget(depend).append(D.Format(R.string.target_in))
                val type = D.Format(if (actionDetail1 == 2001) R.string.magic else R.string.physical)
                val id = R.string.action_branch_exists_target1_type2_p3
                setBranch(
                    branch,
                    D.Format(id, arrayOf(target, type, D.Format(R.string.action_branch_exists_type_yes).tag(true))),
                    D.Format(id, arrayOf(target, type, D.Format(R.string.action_branch_exists_type_no).tag(false)))
                )
            }
        }
        //グレーターゴーレム
        1900 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_barrier_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_barrier_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_barrier_no).tag(false)))
            )
        }
        // ミミ（サマー）
        1800 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_multi_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_multi_target_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_multi_target_no).tag(false)))
            )
        }
        // キャル（サマー）
        1700 -> {
            val target = getTarget(depend)
            var detail1 = actionValue3.toInt()
            val isUp = isStatusUp(detail1)
            if (detail1 > 1000) {
                detail1 -= 1000
            }
            val content = D.Join(arrayOf(
                getStatusContent(detail1 / 10),
                D.Format(if (isUp) R.string.content_up else R.string.content_down)
            ))
            val id = R.string.action_branch_status_change_target1_content2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(target, content, D.Format(R.string.action_branch_status_change_yes).tag(true))),
                D.Format(id, arrayOf(target, content, D.Format(R.string.action_branch_status_change_no).tag(false)))
            )
        }
        // イノリ（怪盗）
        1601 -> {
            setStateBranch(branch,actionDetail1 - 1600, actionValue3)
        }
        in 1501..1599 -> {
            val target = getTarget(depend)
            val state = getAbnormalContent(actionDetail1 - 1500)
            setAbnormalBranch(branch, target, state)
        }
        // アリサ、カヤ、スズナ（サマー）、ルカ（サマー）、クロエ（聖学祭）
        in 1200..1299 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionDetail1 / 10 % 10).toString())))
            val count = D.Text((actionDetail1 % 10).toString())
            val id = R.string.action_branch_counter1_count2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(counter, count, D.Format(R.string.action_branch_gt).tag(true))),
                D.Format(id, arrayOf(counter, count, D.Format(R.string.action_branch_lt).tag(false)))
            )
        }
        // リノ（ワンダー）
        1001 -> {
            val id = R.string.action_branch_critical_p1
            setBranch(
                branch,
                D.Format(id, arrayOf(D.Format(R.string.action_branch_critical_yes).tag(true))),
                D.Format(id, arrayOf(D.Format(R.string.action_branch_critical_no).tag(false))),
            )
        }
        // エリコ、ニノン、シノブ（ハロウィン）
        1000 -> {
            val id = R.string.action_branch_overthrew_p1
            setBranch(
                branch,
                D.Format(id, arrayOf(D.Format(R.string.action_branch_overthrew_yes).tag(true))),
                D.Format(id, arrayOf(D.Format(R.string.action_branch_overthrew_no).tag(false)))
            )
        }
        // ぺコリーヌ（ニューイヤー）
        in 901..999 -> {
            val target = getTarget(depend)
            val value = D.Text("${actionDetail1 % 100}%")
            val id = R.string.action_branch_hp_target1_value2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(target, value, D.Format(R.string.action_branch_lt).tag(true))),
                D.Format(id, arrayOf(target, value, D.Format(R.string.action_branch_gt).tag(false)))
            )
        }
        // フブキ
        900 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_hp_max_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_hp_max_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_hp_max_no).tag(false)))
            )
        }
        // ホマレ
        721 -> {
            setStateBranch(branch, actionValue3.toInt(), actionValue4)
        }
        // ランファ
        720 -> {
            val summon = getUnitName(actionValue3.toInt())
            val id = R.string.action_branch_summon1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(summon, D.Format(R.string.action_branch_summon_yes).tag(true))),
                D.Format(id, arrayOf(summon, D.Format(R.string.action_branch_summon_no).tag(false)))
            )
        }
        // 水瓶座
        710 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_break_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_break_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_break_no).tag(false)))
            )
        }
        // 魚座、蠍座
        in 701..709 -> {
            val target = getAssignmentSide()
            val count = D.Text((actionDetail1 - 700).toString())
            val id = R.string.action_branch_unit_target1_count2_p3
            setBranch(
                branch,
                D.Format(id, arrayOf(target, count, D.Format(R.string.action_branch_unit_yes).tag(true))),
                D.Format(id, arrayOf(target, count, D.Format(R.string.action_branch_unit_no).tag(false)))
            )
        }
        // マコト（サマー）、アンナ（サマー）
        700 -> {
            val id = R.string.action_branch_single_target_p1
            setBranch(
                branch,
                D.Format(id, arrayOf(D.Format(R.string.action_branch_single_target_yes).tag(true))),
                D.Format(id, arrayOf(D.Format(R.string.action_branch_single_target_no).tag(false)))
            )
        }
        // レイ、ルナ、クリスティーナ（クリスマス）、チエル（聖学祭）
        in 600..699 -> {
            setStateBranch(branch,actionDetail1 - 600, actionValue3)
        }
        in 500..599 -> {
            val target = getTarget(depend)
            setAbnormalDamageBranch(branch, target)
        }
        // カヤ（タイムトラベル）
        101 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_speed_up_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_speed_up_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_speed_up_no).tag(false)))
            )
        }
        // ミフユ
        100 -> {
            val target = getTarget(depend)
            val id = R.string.action_branch_akinesia_target1_p2
            setBranch(
                branch,
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_akinesia_yes).tag(true))),
                D.Format(id, arrayOf(target, D.Format(R.string.action_branch_akinesia_no).tag(false)))
            )
        }
        // スズメ
        in 1..99 -> {
            val event = D.Text((actionId % 10).toString())
            val id = R.string.action_branch_odds1_event2_p3
            setBranch(
                branch,
                D.Format(
                    id,
                    arrayOf(D.Text("${actionDetail1}%"), event, D.Format(R.string.action_branch_event_yes).tag(true))
                ),
                D.Format(
                    id,
                    arrayOf(D.Text("${100 - actionDetail1}%"), event, D.Format(R.string.action_branch_event_no).tag(false))
                )
            )
        }
    }

    return branch.toTypedArray()
}

private fun SkillAction.getCounterBranch(): Array<Pair<Int, D>> {
    if (actionDetail1 == 14) {
        val desc = D.Format(
            R.string.action_branch_controled_time1,
            arrayOf(D.Text(actionValue4.toNumStr()))
        )
        return arrayOf(
            actionDetail2 to desc,
            actionDetail3 to desc
        )
    }
    // actionDetail1 == 2
    return arrayOf(
        actionDetail2 to D.Format(
            R.string.action_branch_striked_time1,
            arrayOf(D.Text(actionValue4.toNumStr()))
        )
    )
}

private fun SkillAction.getExistsFieldBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()
    val content = getSkillLabel(actionDetail1)
    val id = R.string.action_branch_field_content1_p2
    setBranch(
        branch,
        D.Format(id, arrayOf(content, D.Format(R.string.action_branch_field_yes).tag(true))),
        D.Format(id, arrayOf(content, D.Format(R.string.action_branch_field_no).tag(false)))
    )
    return branch.toTypedArray()
}

//未知
private fun SkillAction.getDamageReceivedBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()
    val time = D.Text(actionValue2.toNumStr())
    val value = D.Text(actionValue3.toNumStr())
    val id = R.string.action_branch_damage_received_time1_value2_p3
    setBranch(
        branch,
        D.Format(id, arrayOf(time, value, D.Format(R.string.action_branch_damage_received_yes).tag(true))),
        D.Format(id, arrayOf(time, value, D.Format(R.string.action_branch_damage_received_no).tag(false)))
    )
    return branch.toTypedArray()
}

private fun SkillAction.getSingleBranch(): Array<Pair<Int, D>> {
    val desc = when (actionDetail1) {
        //クルル
        1 -> D.Format(
            R.string.action_branch_status_up_target1_max2,
            arrayOf(getTarget(depend), D.Text(actionValue3.toNumStr()))
        )
        //ヤマト
        2 -> D.Format(R.string.action_branch_attack_critical_target1, arrayOf(getTarget(depend)))
        else -> D.Format(R.string.action_branch_unknown)
    }
    return arrayOf(actionDetail2 to desc)
}

private fun SkillAction.setBranch(branch: MutableList<Pair<Int, D>>, yes: D, not: D) {
    if (actionDetail2 != 0) {
        branch.add(actionDetail2 to yes)
    }
    if (actionDetail3 != 0) {
        branch.add(actionDetail3 to not)
    }
}

private fun SkillAction.setStateBranch(branch: MutableList<Pair<Int, D>>, stateId: Int, stateCount: Double) {
    val state = getMarkContent(stateId)
    val target = getTarget(depend)
    if (stateCount == 0.0 || stateCount == 1.0) {
        val id = R.string.action_branch_mark_target1_state2_p3
        setBranch(
            branch,
            D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_mark_yes).tag(true))),
            D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_mark_no).tag(false)))
        )
    } else {
        val count = D.Text(stateCount.toNumStr())
        val id = R.string.action_branch_mark_target1_state2_count3_p4
        setBranch(
            branch,
            D.Format(id, arrayOf(target, state, count, D.Format(R.string.action_branch_gt).tag(true))),
            D.Format(id, arrayOf(target, state, count, D.Format(R.string.action_branch_lt).tag(false)))
        )
    }
}

private fun SkillAction.setAbnormalBranch(branch: MutableList<Pair<Int, D>>, target: D, state: D) {
    val id = R.string.action_branch_abnormal_target1_state2_p3
    setBranch(
        branch,
        D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_abnormal_yes).tag(true))),
        D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_abnormal_no).tag(false)))
    )
}

private fun SkillAction.setAbnormalDamageBranch(branch: MutableList<Pair<Int, D>>, target: D) {
    val state = when (actionDetail1 - 500) {
        0 -> D.Format(R.string.burn)
        1 -> D.Format(R.string.curse)
        2 -> D.Format(R.string.poison)
        3 -> D.Format(R.string.fierce_poison)
        4 -> D.Format(R.string.beshrew)
        11 -> D.Format(R.string.curse_or_beshrew)
        12 -> D.Format(R.string.poison_or_fierce_poison)
        99 -> D.Format(R.string.poison_or_fierce_poison_or_curse_or_beshre_or_burn)
        else -> D.Unknown
    }
    val id = R.string.action_branch_abnormal_target1_state2_p3
    var yes = actionDetail2
    var no = actionDetail3
    if (actionId == 104001201) {// TODO アオイ S1+ 谜之顺序
        yes = actionDetail3
        no = actionDetail2
    }
    if (yes != 0) {
        branch.add(
            yes to D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_abnormal_yes).tag(true)))
        )
    }
    if (no != 0) {
        branch.add(
            no to D.Format(id, arrayOf(target, state, D.Format(R.string.action_branch_abnormal_no).tag(false)))
        )
    }
}
