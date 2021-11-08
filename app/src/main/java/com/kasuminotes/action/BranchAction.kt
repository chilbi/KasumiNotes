package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getBranch(): Array<Pair<Int, D>> {
    return when (actionType) {
        23 -> getDependBranch()
        28 -> getNoDependBranch()
        42 -> getStrikedBranch()
        53 -> getExistsFieldBranch()
        else -> emptyArray()
    }
}

fun SkillAction.getDependBranch(): Array<Pair<Int, D>> {
    var branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
        // ホマレ
        in 6000..6999 -> {
            branch = getStateBranch(actionDetail1 - 6000, actionValue3)
        }
        // タマキ、ミサト（サマー）
        1300 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_physical_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_magic_target1,
                        arrayOf(target)
                    )
                )
            }
        }
        // ぺコリーヌ（プリンセス）、レイ（ハロウィン）
        in 900..999 -> {
            val above = D.Text("${actionDetail1 % 100}%")
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_hp_not_target1_above2,
                        arrayOf(target, above)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_hp_target1_above2,
                        arrayOf(target, above)
                    )
                )
            }
        }
        // マコト（サマー）
        700 -> {
            if (actionDetail2 != 0) {
                branch.add(actionDetail2 to D.Format(R.string.action_branch_single_target))
            }
            if (actionDetail3 != 0) {
                branch.add(actionDetail3 to D.Format(R.string.action_branch_not_single_target))
            }
        }
        // ルナ、クリスティーナ（クリスマス）
        in 600..699 -> {
            branch = getStateBranch(actionDetail1 - 600, actionValue3)
        }
        // アオイ、アオイ（編入生）、ミツキ（オーエド）
        in 500..599 -> {
            val target = getTarget(depend)
            val state = when (actionDetail1 - 500) {
                0 -> D.Format(R.string.burn)
                1 -> D.Format(R.string.curse)
                2 -> D.Format(R.string.poison)
                3 -> D.Format(R.string.fierce_poison)
                12 -> D.Format(R.string.poison_or_fierce_poison)
                else -> D.Unknown
            }
            var trueBranch = actionDetail2
            var falseBranch = actionDetail3
            if (actionId == 104001201) {// TODO アオイ Main1+ 谜之顺序
                trueBranch = actionDetail3
                falseBranch = actionDetail2
            }
            if (trueBranch != 0) {
                branch.add(
                    trueBranch to D.Format(
                        R.string.action_branch_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
            if (falseBranch != 0) {
                branch.add(
                    falseBranch to D.Format(
                        R.string.action_branch_not_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
        }
        // イオ
        300 -> {
            val target = getTarget(depend)
            val state = D.Format(R.string.charm)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_target1_state2,
                        arrayOf(target, state)
                    )
                )
            }
        }
        // カヤ（タイムトラベル）
        101 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_speed_up_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_speed_up_target1,
                        arrayOf(target)
                    )
                )
            }
        }
        // レム
        100 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_akinesia_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_akinesia_target1,
                        arrayOf(target)
                    )
                )
            }
        }
    }

    return branch.toTypedArray()
}

fun SkillAction.getNoDependBranch(): Array<Pair<Int, D>> {
    var branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
        // アリサ、カヤ、スズナ（サマー）、ルカ（サマー）、クロエ（聖学祭）
        in 1200..1299 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionDetail1 / 10 % 10).toString())))
            val above = D.Text((actionDetail1 % 10).toString())
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_counter1_above2,
                        arrayOf(counter, above)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_counter1_above2,
                        arrayOf(counter, above)
                    )
                )
            }
        }
        // リノ（ワンダー）
        1001 -> {
            if (actionDetail2 != 0) {
                branch.add(actionDetail2 to D.Format(R.string.action_branch_critical))
            }
            if (actionDetail3 != 0) {
                branch.add(actionDetail3 to D.Format(R.string.action_branch_not_critical))
            }
        }
        // エリコ、ニノン、シノブ（ハロウィン）
        1000 -> {
            if (actionDetail2 != 0) {
                branch.add(actionDetail2 to D.Format(R.string.action_branch_overthrew))
            }
            if (actionDetail3 != 0) {
                branch.add(actionDetail3 to D.Format(R.string.action_branch_not_overthrew))
            }
        }
        // ぺコリーヌ（ニューイヤー）
        in 900..999 -> {
            val above = D.Text("${actionDetail1 % 100}%")
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_hp_not_target1_above2,
                        arrayOf(target, above)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_hp_target1_above2,
                        arrayOf(target, above)
                    )
                )
            }
        }
        // ホマレ
        721 -> {
            branch = getStateBranch(actionValue3.toInt(), actionValue4)
        }
        // マコト（サマー）、アンナ（サマー）
        700 -> {
            if (actionDetail2 != 0) {
                branch.add(actionDetail2 to D.Format(R.string.action_branch_single_target))
            }
            if (actionDetail3 != 0) {
                branch.add(actionDetail3 to D.Format(R.string.action_branch_not_single_target))
            }
        }
        // レイ、ルナ、クリスティーナ（クリスマス）、チエル（聖学祭）
        in 600..699 -> {
            branch = getStateBranch(actionDetail1 - 600, actionValue3)
        }
        // ミフユ
        100 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_akinesia_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_akinesia_target1,
                        arrayOf(target)
                    )
                )
            }
        }
        // スズメ
        in 1..99 -> {
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_success_odds1,
                        arrayOf(D.Text("${actionDetail1}%"))
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_failure_odds1,
                        arrayOf(D.Text("${100 - actionDetail1}%"))
                    )
                )
            }
        }
    }

    return branch.toTypedArray()
}

fun SkillAction.getStrikedBranch(): Array<Pair<Int, D>> {
    return arrayOf(
        actionDetail2 to D.Format(
            R.string.action_striked_branch_time1,
            arrayOf(D.Text(actionValue4.toNumStr()))
        )
    )
}

fun SkillAction.getExistsFieldBranch(): Array<Pair<Int, D>> {
    val content = getSkillLabel(actionDetail1)

    return arrayOf(
        actionDetail2 to D.Format(R.string.action_exists_field_branch_content1, arrayOf(content)),
        actionDetail3 to D.Format(R.string.action_not_exists_field_branch_content1, arrayOf(content))
    )
}

private fun SkillAction.getStateBranch(stateId: Int, stateCount: Double): MutableList<Pair<Int, D>> {
    val branch = mutableListOf<Pair<Int, D>>()
    val state = getStateContent(stateId)
    val target = getTarget(depend)
    if (stateCount == 0.0 || stateCount == 1.0) {
        if (actionDetail2 != 0) {
            branch.add(
                actionDetail2 to D.Format(
                    R.string.action_branch_have_target1_state2,
                    arrayOf(target, state)
                )
            )
        }
        if (actionDetail3 != 0) {
            branch.add(
                actionDetail3 to D.Format(
                    R.string.action_branch_not_have_target1_state2,
                    arrayOf(target, state)
                )
            )
        }
    } else {
        val above = D.Text(stateCount.toNumStr())
        if (actionDetail2 != 0) {
            branch.add(
                actionDetail2 to D.Format(
                    R.string.action_branch_target1_state2_above3,
                    arrayOf(target, state, above)
                )
            )
        }
        if (actionDetail3 != 0) {
            branch.add(
                actionDetail3 to D.Format(
                    R.string.action_branch_not_target1_state2_above3,
                    arrayOf(target, state, above)
                )
            )
        }
    }
    return branch
}
