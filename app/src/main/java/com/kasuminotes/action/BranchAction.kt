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
    val branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
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
            val state = getStateContent(actionDetail1 % 100)
            val target = getTarget(depend)
            if (actionValue3 == 0.0 || actionValue3 == 1.0) {
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
                val above = D.Text(actionValue3.toNumStr())
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
        }
        // アオイ（編入生）
        512 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_fierce_poison_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_fierce_poison_target1,
                        arrayOf(target)
                    )
                )
            }
        }
        // アオイ
        502 -> {
            val target = getTarget(depend)
            // TODO アオイ Main1+ 谜之顺序
            var d2 = actionDetail2
            var d3 = actionDetail3
            if (actionId == 104001201) {
                d2 = actionDetail3
                d3 = actionDetail2
            }
            if (d2 != 0) {
                branch.add(
                    d2 to D.Format(
                        R.string.action_branch_poison_target1,
                        arrayOf(target)
                    )
                )
            }
            if (d3 != 0) {
                branch.add(
                    d3 to D.Format(
                        R.string.action_branch_not_poison_target1,
                        arrayOf(target)
                    )
                )
            }
        }
        // イオ
        300 -> {
            val target = getTarget(depend)
            if (actionDetail2 != 0) {
                branch.add(
                    actionDetail2 to D.Format(
                        R.string.action_branch_charm_target1,
                        arrayOf(target)
                    )
                )
            }
            if (actionDetail3 != 0) {
                branch.add(
                    actionDetail3 to D.Format(
                        R.string.action_branch_not_charm_target1,
                        arrayOf(target)
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
    val branch = mutableListOf<Pair<Int, D>>()

    when (actionDetail1) {
        // アリサ、カヤ、スズナ（サマー）、ルカ（サマー）、クロエ（聖学祭）
        in 1200..1299 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionDetail1 / 10 % 10).toString())))
            val above = D.Text(actionValue3.toNumStr())
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
            val state = getStateContent(actionDetail1 % 100)
            val target = getTarget(depend)
            if (actionValue3 == 0.0 || actionValue3 == 1.0) {
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
                val above = D.Text(actionValue3.toNumStr())
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
