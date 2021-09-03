package com.kasuminotes.action

import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

fun SkillAction.getTarget(depend: SkillAction?): D {
    if (depend != null) {
        return if (depend.actionType == 1 || depend.actionId == actionId) {
            D.Format(R.string.target_damaged)
        } else if (depend.actionType == 7) {
            if (arrayOf(37, 38, 39).contains(actionType)) {
                depend.getTarget(null)
            } else {
                depend.getFocus().append(getTarget(null))
            }
        } else if (
            depend.actionType == 23 &&
            depend.targetCount == 99/* &&
            (
                depend.actionDetail1 == 100 ||//レム
                depend.actionDetail1 == 300 ||//イオ
                depend.actionDetail1 == 501 ||//ミツキ（オーエド）
                depend.actionDetail1 == 502 ||//アオイ
                depend.actionDetail1 in 900..999//レイ（ハロウィン）
            )*/
        ) {
            D.Format(R.string.target_eligible)
        } else if (depend.depend != null) {
            depend.getTarget(depend.depend)
        } else {
            //D.Unknown
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
                                    D.Format(R.string.target_forefront_enemy)
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
                    if (targetRange == -1 || targetRange >= 2160) {
                        D.Format(
                            R.string.target_front_all_content1,
                            arrayOf(getAssignment())
                        )
                    } else {
                        if (actionType == 23/* && (actionDetail1 == 100 || actionDetail1 == 501 || actionDetail1 in 900..999)*/) {//レム、レイ（ハロウィン）、ミツキ（オーエド）
                            D.Join(
                                arrayOf(
                                    D.Format(R.string.target_front),
                                    D.Format(
                                        R.string.target_range1_content2,
                                        arrayOf(
                                            D.Text(targetRange.toString()),
                                            getAssignment()
                                        )
                                    ),
                                    D.Format(
                                        R.string.target_any_content1,
                                        arrayOf(getAssignment())
                                    )
                                )
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
                    }
                } else {
                    D.Unknown
                }
            } else if (targetArea == 2) {
                if (targetCount == 99) {
                    if ((targetRange == -1 || targetRange >= 2160)) {
                        D.Format(
                            R.string.target_all_content1,
                            arrayOf(getAssignment())
                        )
                    } else {
                        if (actionType == 23/* && (actionDetail1 == 300 || actionDetail1 == 502)*/) {// イオ、アオイ
                            D.Join(
                                arrayOf(
                                    D.Format(
                                        R.string.target_range1_content2,
                                        arrayOf(
                                            D.Text(targetRange.toString()),
                                            getAssignment()
                                        )
                                    ),
                                    D.Format(
                                        R.string.target_any_content1,
                                        arrayOf(getAssignment())
                                    )
                                )
                            )
                        } else {
                            D.Format(
                                R.string.target_all_range1_content2,
                                arrayOf(
                                    D.Text(targetRange.toString()),
                                    getAssignment()
                                )
                            )
                        }
                    }
                } else if (targetCount == 1 && (targetRange == -1 || targetRange >= 2160)) {
                    if (targetNumber == 0) {
                        if (actionType == 7) {
                            D.join(R.string.target_nearest, getAssignmentResId())
                        } else {
                            D.Format(
                                R.string.target_one_content1,
                                arrayOf(getAssignment())
                            )
                        }
                    } else {
                        D.Format(
                            R.string.target_near_number1_content2,
                            arrayOf(
                                D.Text((targetNumber + 1).toString()),
                                getAssignment()
                            )
                        )
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
                    D.Unknown
                }
            } else if (targetArea == 3) {
                if (targetCount == 99) {
                    when {
                        targetRange >= 2160 || targetRange <= 0 -> {
                            D.Format(
                                R.string.target_all_content1,
                                arrayOf(getAssignment())
                            )
                        }
                        targetRange > 0 -> {
                            D.Format(
                                R.string.target_all_range1_content2,
                                arrayOf(
                                    D.Text(targetRange.toString()),
                                    getAssignment()
                                )
                            )
                        }
                        else -> {
                            D.Unknown
                        }
                    }
                } else if (targetCount == 1) {
                    if (targetRange >= 2160 && targetNumber > 0) {
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
                    } else if (targetRange == 0 && targetNumber == 0) {
                        D.Format(R.string.target_self)
                    } else {
                        D.Unknown
                    }
                } else {
                    D.Unknown
                }
            } else {
                D.Unknown
            }
        }
        2 -> {
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
            if (targetArea == 1 && targetCount == 1) {
                if (targetRange == -1 || targetRange >= 2160) {
                    D.join(R.string.target_front, R.string.target_farthest, R.string.target_enemy)
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
            } else if (targetArea == 2 && targetCount == 1) {
                if (targetAssignment == 2) {
                    D.Format(R.string.target_farthest_friendly)
                } else {
                    D.join(R.string.target_farthest, R.string.target_enemy)
                }
            } else if (targetArea == 3 && targetCount == 1) {
                D.join(R.string.target_farthest, getAssignmentResId())
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
        10 -> {
//            if (
//                (targetArea == 1 || targetArea == 2) &&
//                targetCount == 1 &&
//                (targetRange == -1 || targetRange >= 2160)
//            ) {
//                if (targetAssignment == 1) {
//                    D.Format(R.string.target_forefront_enemy)
//                } else {
//                    D.Format(R.string.target_forefront_friendly)
//                }
//            } else {
//                D.Unknown
//            }
            if (targetAssignment == 1) {
                D.Format(R.string.target_forefront_enemy)
            } else {
                D.Format(R.string.target_forefront_friendly)
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
                            D.Format(R.string.hp_ratio),
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
