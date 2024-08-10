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
        111 -> getStatusUpBranch()
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
        in 6000..6999 -> {
            setStateBranch(branch,actionDetail1 - 6000, actionValue3)
        }
        // アメス
        1900 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_barrier_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_barrier_target1, arrayOf(target))
            )
        }
        // ミミ（サマー）
        1800 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_multi_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_multi_target1, arrayOf(target))
            )
        }
        // キャル（オーバーロード）
        1600 -> {
            val state = D.Format(R.string.fear)
            setBranch(
                branch,
                D.Format(R.string.action_branch_target1_state2, arrayOf(target, state)),
                D.Format(R.string.action_branch_not_target1_state2, arrayOf(target, state))
            )
        }
        // タマキ、ミサト（サマー）
        1300 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_physical_target1, arrayOf(target)),
                D.Format(R.string.action_branch_magic_target1, arrayOf(target))
            )
        }
        // ぺコリーヌ（プリンセス）、レイ（ハロウィン）
        in 900..999 -> {
            val above = D.Text("${actionDetail1 % 100}%")
            setBranch(
                branch,
                D.Format(R.string.action_branch_not_hp_target1_above2, arrayOf(target, above)),
                D.Format(R.string.action_branch_hp_target1_above2, arrayOf(target, above))
            )
        }
        // 天秤座
        710 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_break_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_break_target1, arrayOf(target))
            )
        }
        // マコト（サマー）
        700 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_single_target),
                D.Format(R.string.action_branch_not_single_target)
            )
        }
        // ルナ、クリスティーナ（クリスマス）
        in 600..699 -> {
            setStateBranch(branch,actionDetail1 - 600, actionValue3)
        }
        // アオイ、アオイ（編入生）、ミツキ（オーエド）
        in 500..599 -> {
            val state = when (actionDetail1 - 500) {
                0 -> D.Format(R.string.burn)
                1 -> D.Format(R.string.curse)
                2 -> D.Format(R.string.poison)
                3 -> D.Format(R.string.fierce_poison)
                4 -> D.Format(R.string.beshrew)
                11 -> D.Format(R.string.curse_or_beshrew)
                12 -> D.Format(R.string.poison_or_fierce_poison)
                else -> D.Unknown
            }
            var yes = actionDetail2
            var not = actionDetail3
            if (actionId == 104001201) {// TODO アオイ S1+ 谜之顺序
                yes = actionDetail3
                not = actionDetail2
            }
            if (yes != 0) {
                branch.add(
                    yes to D.Format(
                        R.string.action_branch_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
            if (not != 0) {
                branch.add(
                    not to D.Format(
                        R.string.action_branch_not_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
        }
        // イオ
        300 -> {
            val state = D.Format(R.string.charm)
            setBranch(
                branch,
                D.Format(R.string.action_branch_target1_state2, arrayOf(target, state)),
                D.Format(R.string.action_branch_not_target1_state2, arrayOf(target, state))
            )
        }
        // 射手座
        200 -> {
            val state = D.Format(R.string.darkness)
            setBranch(
                branch,
                D.Format(R.string.action_branch_target1_state2, arrayOf(target, state)),
                D.Format(R.string.action_branch_not_target1_state2, arrayOf(target, state))
            )
        }
        // カヤ（タイムトラベル）
        101 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_speed_up_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_speed_up_target1, arrayOf(target))
            )
        }
        // レム
        100 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_akinesia_target1, arrayOf(target)
                ),
                D.Format(R.string.action_branch_not_akinesia_target1, arrayOf(target))
            )
        }
    }

    return branch.toTypedArray()
}

private fun SkillAction.getNoDependBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
        // ルカ（ニューイヤー）、シノブ
        in 6000..6999 -> {
            setStateBranch(branch,actionDetail1 - 6000, actionValue3)
        }
        // ライラエル
        in 3000..3999 -> {
            val target = getTarget(depend)
            val state = getStateContent(actionDetail1 - 3000, actionId)
            setBranch(
                branch,
                D.Format(R.string.action_branch_target1_environment2, arrayOf(target, state)),
                D.Format(R.string.action_branch_not_target1_environment2, arrayOf(target, state))
            )
        }
        // アキノ（サマー）、ハツネ（ニューイヤー）
        2000, 2001 -> {
            if (targetCount == 1) {
                val target = getTarget(depend)
                val physical = D.Format(R.string.action_branch_physical_target1, arrayOf(target))
                val magic = D.Format(R.string.action_branch_magic_target1, arrayOf(target))
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
                setBranch(
                    branch,
                    D.Format(R.string.action_branch_exists_target1_type2, arrayOf(target, type)),
                    D.Format(R.string.action_branch_not_exists_target1_type2, arrayOf(target, type))
                )
            }
        }
        // ミミ（サマー）
        1800 -> {
            val target = getTarget(depend)
            setBranch(
                branch,
                D.Format(R.string.action_branch_multi_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_multi_target1, arrayOf(target))
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
            setBranch(
                branch,
                D.Format(R.string.action_branch_status_change_target1_content2_, arrayOf(target, content)),
                D.Format(R.string.action_branch_not_status_change_target1_content2, arrayOf(target, content))
            )
        }
        // イノリ（怪盗）
        1601 -> {
            setStateBranch(branch,actionDetail1 - 1600, actionValue3)
        }
        // アリサ、カヤ、スズナ（サマー）、ルカ（サマー）、クロエ（聖学祭）
        in 1200..1299 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionDetail1 / 10 % 10).toString())))
            val above = D.Text((actionDetail1 % 10).toString())
            setBranch(
                branch,
                D.Format(R.string.action_branch_counter1_above2, arrayOf(counter, above)),
                D.Format(R.string.action_branch_not_counter1_above2, arrayOf(counter, above))
            )
        }
        // リノ（ワンダー）
        1001 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_critical),
                D.Format(R.string.action_branch_not_critical)
            )
        }
        // エリコ、ニノン、シノブ（ハロウィン）
        1000 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_overthrew),
                D.Format(R.string.action_branch_not_overthrew)
            )
        }
        // ぺコリーヌ（ニューイヤー）
        in 900..999 -> {
            val target = getTarget(depend)
            val above = D.Text("${actionDetail1 % 100}%")
            setBranch(
                branch,
                D.Format(R.string.action_branch_not_hp_target1_above2, arrayOf(target, above)),
                D.Format(R.string.action_branch_hp_target1_above2, arrayOf(target, above))
            )
        }
        // ホマレ
        721 -> {
            setStateBranch(branch, actionValue3.toInt(), actionValue4)
        }
        // ランファ
        720 -> {
            val summon = getUnitName(actionValue3.toInt())
            setBranch(
                branch,
                D.Format(R.string.action_branch_exist_summon1, arrayOf(summon)),
                D.Format(R.string.action_branch_not_exist_summon1, arrayOf(summon))
            )
        }
        // 水瓶座
        710 -> {
            val target = getTarget(depend)
            setBranch(
                branch,
                D.Format(R.string.action_branch_break_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_break_target1, arrayOf(target))
            )
        }
        // 魚座、蠍座
        in 701..709 -> {
            val target = getAssignmentSide()
            val count = D.Text((actionDetail1 - 700).toString())
            setBranch(
                branch,
                D.Format(R.string.action_branch_unit_target1_count2, arrayOf(target, count)),
                D.Format(R.string.action_branch_not_unit_target1_count2, arrayOf(target, count))
            )
        }
        // マコト（サマー）、アンナ（サマー）
        700 -> {
            setBranch(
                branch,
                D.Format(R.string.action_branch_single_target),
                D.Format(R.string.action_branch_not_single_target)
            )
        }
        // レイ、ルナ、クリスティーナ（クリスマス）、チエル（聖学祭）
        in 600..699 -> {
            setStateBranch(branch,actionDetail1 - 600, actionValue3)
        }
        // カヤ（タイムトラベル）
        101 -> {
            val target = getTarget(depend)
            setBranch(
                branch,
                D.Format(R.string.action_branch_speed_up_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_speed_up_target1, arrayOf(target))
            )
        }
        // ミフユ
        100 -> {
            val target = getTarget(depend)
            setBranch(
                branch,
                D.Format(R.string.action_branch_akinesia_target1, arrayOf(target)),
                D.Format(R.string.action_branch_not_akinesia_target1, arrayOf(target))
            )
        }
        // スズメ
        in 1..99 -> {
            val event = (actionId % 10).toString()
            setBranch(
                branch,
                D.Format(
                    R.string.action_branch_success_odds1_event2,
                    arrayOf(D.Text("${actionDetail1}%"), D.Text(event))
                ),
                D.Format(
                    R.string.action_branch_failure_odds1_event2,
                    arrayOf(D.Text("${100 - actionDetail1}%"), D.Text(event))
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
    setBranch(
        branch,
        D.Format(R.string.action_branch_exists_field_content1, arrayOf(content)),
        D.Format(R.string.action_branch_not_exists_field_content1, arrayOf(content))
    )
    return branch.toTypedArray()
}

private fun SkillAction.getDamageReceivedBranch(): Array<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()
    val time = D.Text(actionValue2.toNumStr())
    val value = D.Text(actionValue3.toNumStr())
    setBranch(
        branch,
        D.Format(R.string.action_branch_damage_received_time1_value2, arrayOf(time, value)),
        D.Format(R.string.action_branch_not_damage_received_time1_value2, arrayOf(time, value))
    )
    return branch.toTypedArray()
}

private fun SkillAction.getStatusUpBranch(): Array<Pair<Int, D>> {
    val desc = D.Format(
        R.string.action_branch_status_up_target1_max2,
        arrayOf(getTarget(depend), D.Text(actionValue3.toNumStr()))
    )
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
    val state = getStateContent(stateId, actionId)
    val target = getTarget(depend)
    if (stateCount == 0.0 || stateCount == 1.0) {
        setBranch(
            branch,
            D.Format(R.string.action_branch_have_target1_state2, arrayOf(target, state)),
            D.Format(R.string.action_branch_not_have_target1_state2, arrayOf(target, state))
        )
    } else {
        val above = D.Text(stateCount.toNumStr())
        setBranch(
            branch,
            D.Format(R.string.action_branch_target1_state2_above3, arrayOf(target, state, above)),
            D.Format(R.string.action_branch_not_target1_state2_above3, arrayOf(target, state, above))
        )
    }
}
