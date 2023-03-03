package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTarget(depend: SkillAction?, focused: Boolean = false): D {
    if (depend != null) {
        return if (depend.actionType == 1 || depend.actionId == actionId) {
            D.Format(R.string.target_damaged)
        } else if (depend.actionType == 7) {
            if (arrayOf(37, 38, 39).contains(actionType) || focused) {
                depend.getTarget(null)
            } else {
                depend.getFocus(targetArea).append(getTarget(null))
            }
        } else if (
            (depend.actionType == 23 || depend.actionType == 28) && depend.targetCount > 1
        ) {
            D.Format(R.string.target_eligible)
        } else if (depend.depend != null) {
            depend.getTarget(depend.depend, focused)
        } else {
            depend.getTarget(null)
        }
    }

    return when (targetType) {
        0, 7 -> D.Format(R.string.target_self)
        1, 3 -> {
            if (targetArea == 1) {
                if (targetCount == 1) {
                    if (targetRange == -1 || targetRange >= 2160) {
                        if (targetNumber > 0) {
                            D.Format(
                                R.string.target_count1_content2,
                                arrayOf(
                                    D.Text((targetNumber + 1).toString()),
                                    getAssignment()
                                )
                            )
                        } else {
                            if (targetAssignment == 1) {
                                if (actionType == 2) {//リマ
                                    D.Format(R.string.target_first_enemy)
                                } else {
                                    D.Format(
                                        R.string.target_one_content1,
                                        arrayOf(D.Format(R.string.target_enemy))
                                    )
                                }
                            } else {//シノブ
                                D.Format(R.string.target_self)
                            }
                        }
                    } else if (targetRange == 0) {
                        D.Format(
                            R.string.target_one_content1,
                            arrayOf(getAssignment())
                        )
                    } else {
                        D.Format(
                            R.string.target_range1_content2,
                            arrayOf(
                                D.Join(
                                    arrayOf(
                                        D.Format(R.string.target_front),
                                        D.Text(targetRange.toString())
                                    )
                                ),
                                D.join(R.string.target_nearest, getAssignmentResId())
                            )
                        )
                    }
                } else if (targetCount == 99) {
                    val allOrRangeTarget = if (targetRange == -1 || targetRange >= 2160) {
                        D.Format(
                            R.string.target_front_all_content1,
                            arrayOf(getAssignment())
                        )
                    } else {
                        D.Join(
                            arrayOf(
                                D.Format(R.string.target_front),
                                D.Format(
                                    R.string.target_all_range1_content2,
                                    arrayOf(
                                        D.Text(targetRange.toString()),
                                        getAssignment()
                                    )
                                )
                            )
                        )
                    }
                    if (actionType == 23) {//レム、レイ（ハロウィン）、ミツキ（オーエド）、33期射手座
                        allOrRangeTarget.append(getAnyTarget())
                    } else {
                        allOrRangeTarget
                    }
                } else {
                    D.Format(
                        R.string.target_more_count1_content2,
                        arrayOf(
                            D.Text(targetCount.toString()),
                            getAssignment()
                        )
                    )
                }
            } else if (targetArea == 2) {
                if (targetCount == 99) {
                    if (targetRange == -1 || targetRange >= 2160) {
                        val allTarget = D.Format(
                            R.string.target_all_content1,
                            arrayOf(getAssignment())
                        )
                        if (actionType == 23 || actionType == 28) {//ホマレ
                            D.Join(arrayOf(allTarget, getAnyTarget()))
                        } else {
                            allTarget
                        }
                    } else {
                        val rangeTarget = D.Format(
                            R.string.target_all_range1_content2,
                            arrayOf(
                                D.Text(targetRange.toString()),
                                getAssignment()
                            )
                        )
                        if (actionType == 23) {// イオ、アオイ
                            D.Join(arrayOf(rangeTarget, getAnyTarget()))
                        } else {
                            rangeTarget
                        }
                    }
                } else if (targetCount == 1 && (targetRange <= 0 || targetRange >= 2160)) {
                    if (targetNumber > 0) {
                        if (targetNumber == 1 && targetAssignment == 2) {
                            D.Format(R.string.target_nearest_friendly)
                        } else {
                            D.Format(
                                R.string.target_near_number1_content2,
                                arrayOf(
                                    D.Text(targetNumber.toString()),
                                    getAssignment()
                                )
                            )
                        }
                    } else {//targetNumber == 0
                        if (actionType == 7) {
                            D.join(R.string.target_nearest, getAssignmentResId())
                        } else {
                            D.Format(
                                R.string.target_one_content1,
                                arrayOf(getAssignment())
                            )
                        }
                    }
                } else if (targetCount == 0 && targetRange > 0) {
                    D.Format(
                        R.string.target_all_range1_content2,
                        arrayOf(
                            D.Text(targetRange.toString()),
                            getAssignment()
                        )
                    )
                } else {
                    if (targetRange <= 0 || targetRange >= 2160) {
                        D.Format(
                            R.string.target_more_count1_content2,
                            arrayOf(
                                D.Text(targetCount.toString()),
                                getAssignment()
                            )
                        )
                    } else {
                        D.Format(
                            R.string.target_range1_count2_content3,
                            arrayOf(
                                D.Text(targetRange.toString()),
                                D.Text(targetCount.toString()),
                                getAssignment()
                            )
                        )
                    }
                }
            } else if (targetArea == 3) {
                if (targetCount == 99) {
                    if (targetRange <= 0 || targetRange >= 2160) {
                        val allTarget = D.Format(
                            R.string.target_all_content1,
                            arrayOf(getAssignment())
                        )
                        if (actionType == 23 || actionType == 28) {//アメス
                            D.Join(arrayOf(allTarget, getAnyTarget()))
                        } else {
                            allTarget
                        }
                    } else {
                        D.Format(
                            R.string.target_all_range1_content2,
                            arrayOf(
                                D.Text(targetRange.toString()),
                                getAssignment()
                            )
                        )
                    }
                } else if (targetCount == 1) {
                    if (targetRange == -1 || targetRange >= 2160) {
                        if (targetNumber > 0) {
                            if (targetNumber == 1 && targetAssignment == 2) {
                                D.Format(R.string.target_nearest_friendly)
                            } else {
                                D.Format(
                                    R.string.target_near_number1_content2,
                                    arrayOf(
                                        D.Text((targetNumber + 1).toString()),
                                        getAssignment()
                                    )
                                )
                            }
                        } else {
                            D.Format(
                                R.string.target_one_content1,
                                arrayOf(getAssignment())
                            )
                        }
                    } else if (targetRange == 0 && targetNumber == 0) {
                        D.Format(R.string.target_self)
                    } else {
                        D.Unknown
                    }
                } else {
                    //イオ　rfMain2、ユカリ rfMain2
                    // D.Format(R.string.target)
                    val targets = D.Format(
                        R.string.target_more_count1_content2,
                        arrayOf(
                            D.Text(targetCount.toString()),
                            getAssignment()
                        )
                    )
                    if (actionType == 23) {
                        D.Join(arrayOf(targets, getAnyTarget()))
                    } else {
                        targets
                    }
                }
            } else {
                D.Unknown
            }
        }
        2, 8 -> {
            val random = if (targetCount == 1) {
                D.Format(
                    R.string.target_random_one_content1,
                    arrayOf(getAssignment())
                )
            } else {
                D.Format(
                    R.string.target_random_count1_content2,
                    arrayOf(
                        D.Text(targetCount.toString()),
                        getAssignment()
                    )
                )
            }
            if (targetArea == 1 && targetRange > 0) {
                D.Join(
                    arrayOf(
                        D.Format(R.string.target_front),
                        if (targetRange < 2160) D.Format(
                            R.string.target_range1_content2,
                            arrayOf(D.Text(targetRange.toString()), random)
                        )
                        else random
                    )
                )
            } else if (targetArea == 2 || targetArea == 3) {
                if (targetRange == -1 || targetRange >= 2160) {
                    random
                } else {
                    D.Format(
                        R.string.target_range1_content2,
                        arrayOf(D.Text(targetRange.toString()), random)
                    )
                }
            } else {
                D.Unknown
            }
        }
        4 -> {
            if (targetArea == 1) {
                if (targetCount == 1) {
                    if (targetRange == -1 || targetRange >= 2160) {
                        D.join(
                            R.string.target_front,
                            R.string.target_farthest,
                            R.string.target_enemy
                        )
                    } else {
                        D.Join(
                            arrayOf(
                                D.Format(R.string.target_front),
                                D.Format(
                                    R.string.target_range1_content2,
                                    arrayOf(
                                        D.Text(targetRange.toString()),
                                        D.join(R.string.target_farthest, getAssignmentResId())
                                    )
                                )
                            )
                        )
                    }
                } else {
                    D.Format(
                        R.string.target_farthest_content1_count2,
                        arrayOf(
                            getAssignment(),
                            D.Text(targetCount.toString())
                        )
                    )
                }
            } else if (targetArea == 2 && targetCount == 1) {
                if (targetAssignment == 2) {
                    D.Format(R.string.target_farthest_friendly)
                } else {
                    D.join(R.string.target_farthest, R.string.target_enemy)
                }
            } else if (targetArea == 3 && targetCount > 0) {
                if (targetNumber > 0) {
                    D.Format(
                        R.string.target_fart_number1_content2,
                        arrayOf(
                            D.Text((targetNumber + 1).toString()),
                            getAssignment()
                        )
                    )
                } else {
                    D.Format(
                        R.string.target_farthest_content1_count2,
                        arrayOf(
                            getAssignment(),
                            D.Text(targetCount.toString())
                        )
                    )
                }
            } else {
                D.Unknown
            }
        }
        5 -> {
            val lowest = D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.hp_ratio),
                    D.Format(R.string.target_low),
                    getAssignment()
                )
            )
            if (targetRange >= 2160) {
                lowest
            } else {
                D.Format(
                    R.string.target_range1_content2,
                    arrayOf(
                        D.Text(targetRange.toString()),
                        lowest
                    )
                )
            }
        }
        6 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.hp_ratio),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        9 -> {
            if (targetAssignment == 1) {
                D.Format(R.string.target_last_enemy)
            } else {
                D.Format(R.string.target_last_friendly)
            }
        }
        10 -> {
            if (targetAssignment == 1) {
                D.Format(R.string.target_first_enemy)
            } else {
                D.Format(R.string.target_first_friendly)
            }
        }
        11 -> {
            D.Format(
                R.string.target_distance1,
                arrayOf(D.Text(actionValue1.toNumStr()))
            )
        }
        12 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.energy),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        13 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.energy),
                    D.Format(R.string.target_low),
                    getAssignment()
                )
            )
        }
        14 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.atk),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        15 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.atk),
                    D.Format(R.string.target_low),
                    getAssignment()
                )
            )
        }
        16 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.magic_str),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        17 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.magic_str),
                    D.Format(R.string.target_low),
                    getAssignment()
                )
            )
        }
        18 -> {
            D.Format(
                R.string.target_all_summon_content1,
                arrayOf(getAssignment())
            )
        }
        20 -> {
            D.Format(
                R.string.target_all_atk_content1,
                arrayOf(getAssignment())
            )
        }
        21 -> {
            D.Format(
                R.string.target_all_magic_str_content1,
                arrayOf(getAssignment())
            )
        }
        24 -> {
            D.Format(R.string.target_boss)
        }
        25 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.hp_ratio),
                    D.Format(R.string.target_low),
                    getAssignment()
                )
            )
        }
        31 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.magic_str),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        34 -> {
            D.Format(R.string.target_without_self)
        }
        35 -> {
            D.Format(
                R.string.target_not_in_back_content1,
                arrayOf(
                    D.Format(
                        R.string.target_most_content1_extent2_target3,
                        arrayOf(
                            D.Format(R.string.hp_remanent),
                            D.Format(R.string.target_high),
                            getAssignment()
                        )
                    )
                )
            )

        }
        37 -> {
            D.Format(
                R.string.target_most_content1_extent2_target3,
                arrayOf(
                    D.Format(R.string.energy),
                    D.Format(R.string.target_high),
                    getAssignment()
                )
            )
        }
        38 -> {
            D.Format(
                R.string.target_highest_atk_or_magic_str_content1,
                arrayOf(getAssignment())
            )
        }
        41 -> {
            D.Format(
                R.string.target_without_self_content1_extent2_count3_target4,
                arrayOf(
                    D.Format(R.string.energy),
                    D.Format(R.string.target_low),
                    D.Text(targetCount.toString()),
                    getAssignment()
                )
            )
        }
        43 -> {
            D.Format(
                R.string.target_without_self_content1_extent2_count3_target4,
                arrayOf(
                    D.Format(R.string.atk),
                    D.Format(R.string.target_high),
                    D.Text(targetCount.toString()),
                    getAssignment()
                )
            )
        }
        else -> D.Unknown
    }
}

fun SkillAction.getAssignmentResId(): Int {
    return when (targetAssignment) {
        1 -> R.string.target_enemy
        else -> R.string.target_friendly
    }
}

fun SkillAction.getAssignment(): D {
    return D.Format(getAssignmentResId())
}

fun SkillAction.getAnyTarget(): D {
    return D.Format(
        R.string.target_any_content1,
        arrayOf(getAssignment())
    )
}
