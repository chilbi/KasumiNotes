package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.SkillAction

/** 残余HP比例最高的 */
private val hpRatioHighTypes = arrayOf(6, 26, 35)
/** 加上残余HP比例最低的 */
private val hpRatioTypes = hpRatioHighTypes.plus(arrayOf(5, 25, 36, 44))
/** TP最高的 */
private val energyHighTypes = arrayOf(12, 27, 37)
/** 加上TP最低的 */
private val energyTypes = energyHighTypes.plus(arrayOf(13, 28, 41))
/** 物理攻击力最高的 */
private val atkHighTypes = arrayOf(14, 29, 43)
/** 加上物理攻击力最低的 */
private val atkTypes = atkHighTypes.plus(arrayOf(15, 30))
/** 魔法攻击力最高的 */
private val magicStrHighTypes = arrayOf(16, 31)
/** 加上魔法攻击力最低的 */
private val magicStrTypes = magicStrHighTypes.plus(arrayOf(17, 32))
/** 物理攻击力或魔法攻击力最高的 */
private val atkOrMagicStrHighTypes = arrayOf(38)
/** 加上物理攻击力或魔法攻击力最低的 */
private val atkOrMagicStrTypes = atkOrMagicStrHighTypes.plus(39)
/** 自身以外的（还有34在其它地方处理了） */
private val withoutSelfTypes = arrayOf(41, 43, 44)
/** 不在自身后面的 */
private val notInBackTypes = arrayOf(35, 36)
/** 所有 "{0}最{1高|低}的{2}" 的类型 */
private val allMostTypes = hpRatioTypes
    .plus(energyTypes)
    .plus(atkTypes)
    .plus(magicStrTypes)
    .plus(atkOrMagicStrTypes)
/** 所有最高的类型 */
private val allMostHighTypes = hpRatioHighTypes
    .plus(energyHighTypes)
    .plus(atkHighTypes)
    .plus(magicStrHighTypes)
    .plus(atkOrMagicStrHighTypes)

fun SkillAction.getTarget(depend: SkillAction?, focused: Boolean = false): D {
    if (depend != null) {
        return if (depend.actionType == 1 || depend.actionId == actionId) {
            getDependMultiTarget(D.Format(R.string.target_damaged))
        } else if (depend.targetType in arrayOf(2, 8) && depend.actionType !in arrayOf(26, 27, 74)) {
            getDependMultiTarget(D.Format(R.string.target_randomized))
        } else if (depend.actionType == 7) {
            if (actionType in arrayOf(37, 38, 39) || focused) {
                depend.getTarget(null, true)
            } else {
                if (targetCount == 1) {
                    depend.getTarget(null, true)
                } else {
                    depend.getTargetFocus(this).append(getTarget(null, true))
                }
            }
        } else if (depend.isBranch()) {
            if (depend.targetCount > 1) {
                getDependMultiTarget(D.Format(R.string.target_eligible))
            } else if (depend.depend != null) {
                getDependMultiTarget(depend.getTarget(depend.depend!!.copy(actionType = 23), focused))
            } else {
                getDependMultiTarget(depend.getTarget(null, focused))
            }
        } else if (depend.actionType == 105) {
            this.copy(targetAssignment = 3).getTarget(null, focused)
        } else if (depend.depend != null) {
            depend.getTarget(depend.depend, focused)
        } else {
            getDependMultiTarget(depend.getTarget(null, focused))
        }
    }

    if (actionType == 105) {
        return this.copy(
            actionType = 0,
            targetType = 3,
            targetCount = 99,
            targetAssignment = 3
        ).getTarget(null, focused)
    }

    return when (targetType) {
        0, 7 -> D.Format(R.string.target_self)
        1, 3, 34 -> {
            val nearTarget = if (targetArea == 1) {
                if (targetCount == 1) {//一名目标
                    if (isFullRangeTarget()) {
                        if (targetNumber > 0) {
                            D.Format(
                                R.string.target_front_number1_assignment2,
                                arrayOf(getNumber(), getAssignmentOne())
                            )
                        } else {//targetNumber == 0
                            D.Join(arrayOf(D.Format(R.string.target_forward), getAssignmentCount()))
                        }
                    } else {
                        D.Format(
                            R.string.target_range1_content2,
                            arrayOf(
                                D.Join(arrayOf(D.Format(R.string.target_front), D.Text(targetRange.toString()))),
                                D.Join(arrayOf(D.Format(R.string.target_nearest), getAssignmentCount()))
                            )
                        )
                    }
                } else {//多名目标
                    val manyTarget = if (targetCount == 99 || targetCount == 0) {
                        if (isFullRangeTarget()) {
                            D.Format(R.string.target_front_all_assignment1, arrayOf(getAssignment()))
                        } else {
                            D.Format(
                                R.string.target_range1_content2,
                                arrayOf(
                                    D.Join(arrayOf(D.Format(R.string.target_front), D.Text(targetRange.toString()))),
                                    D.Format(
                                        R.string.target_range_all_content1,
                                        arrayOf(getAssignment())
                                    )
                                )
                            )
                        }
                    } else {//n名目标
                        if (isFullRangeTarget()) {
                            D.Join(arrayOf(D.Format(R.string.target_forward), getAssignmentCount()))
                        } else {
                            D.Format(
                                R.string.target_range1_content2,
                                arrayOf(
                                    D.Join(arrayOf(D.Format(R.string.target_front), D.Text(targetRange.toString()))),
                                    D.Format(
                                        R.string.target_range_max_assignment_count1,
                                        arrayOf(getAssignmentCount())
                                    )
                                )
                            )
                        }
                    }
                    getAnyManyTarget(manyTarget)//レム、レイ（ハロウィン）、ミツキ（オーエド）、33期射手座
                }
            } else {//targetArea == 2 || targetArea == 3
                if (targetCount == 1) {//一名目标
                    if (isFullRangeTarget()) {
                        if (targetNumber == 0 && targetAssignment != 1) {
                            //targetArea==3、ゴブリングレート（UB）
                            D.Format(R.string.target_self)
                        } else {
                            if (targetNumber > 0) {
                                if (targetNumber == 1 && targetAssignment == 2) {
                                    D.Format(
                                        R.string.target_nearest_assignment1,
                                        arrayOf(getAssignmentCount())
                                    )
                                } else {
                                    D.Format(
                                        R.string.target_near_number1_assignment2,
                                        arrayOf(getNumber(), getAssignmentCount())
                                    )
                                }
                            } else {//targetNumber == 0
                                D.Join(
                                    arrayOf(
                                        D.Format(if (targetArea == 3)
                                            R.string.target_forward else R.string.target_nearest),
                                        getAssignmentCount()
                                    )
                                )
                            }
                        }
                    } else {
                        D.Format(
                            R.string.target_range1_content2,
                            arrayOf(
                                D.Text(targetRange.toString()),
                                D.Join(
                                    arrayOf(
                                        D.Format(R.string.target_nearest),
                                        getAssignmentCount()
                                    )
                                )
                            )
                        )
                    }
                } else {//多名目标
                    val manyTarget = if (targetCount == 99 || targetCount == 0) {
                        if (isFullRangeTarget()) {
                            D.Format(R.string.target_all_content1, arrayOf(getAssignmentSide()))
                        } else {
                            D.Format(
                                R.string.target_range1_content2,
                                arrayOf(
                                    D.Text(targetRange.toString()),
                                    D.Format(
                                        R.string.target_range_all_content1,
                                        arrayOf(getAssignment())
                                    )
                                )
                            )
                        }
                    } else {//n名目标
                        if (isFullRangeTarget()) {
                            D.Join(arrayOf(D.Format(R.string.target_nearest), getAssignmentCount()))
                        } else {
                            D.Format(
                                R.string.target_range1_content2,
                                arrayOf(
                                    D.Text(targetRange.toString()),
                                    D.Format(
                                        R.string.target_range_max_assignment_count1,
                                        arrayOf(getAssignmentCount())
                                    )
                                )
                            )
                        }
                    }
                    getAnyManyTarget(manyTarget)//(targetArea=2)ホマレ、イオ、アオイ; (targetArea=3)アメス、イオ　rf S2、ユカリ rf S2
                }
            }
            if (targetType == 34) {
                D.Join(arrayOf(D.Format(R.string.target_without_self), nearTarget))
            } else {
                nearTarget
            }
        }
        2, 8 -> {
            val randomTarget = D.Format(
                R.string.target_random_assignment_count1,
                arrayOf(getAssignmentCount())
            )
            val rangeRandomTarget = if (isFullRangeTarget()) {
                randomTarget
            } else {
                D.Format(
                    R.string.target_range1_content2,
                    arrayOf(
                        D.Text(targetRange.toString()),
                        randomTarget
                    )
                )
            }
            if (targetArea == 1) {
                D.Join(arrayOf(D.Format(R.string.target_front), rangeRandomTarget))
            } else {
                rangeRandomTarget
            }
        }
        4 -> {
            val farthestTarget = if (targetNumber > 0) {
                D.Format(
                    R.string.target_far_number1_assignment2,
                    arrayOf(
                        getNumber(),
                        getAssignment()
                    )
                )
            } else if (targetCount == 1) {
                D.Format(
                    R.string.target_farthest_assignment1,
                    arrayOf(getAssignmentOne())
                )
            } else {
                D.Format(
                    R.string.target_farthest_assignment_count1,
                    arrayOf(getAssignmentCount())
                )
            }
            val rangeFarthestTarget = if (isFullRangeTarget()) {
                farthestTarget
            } else {
                D.Format(
                    R.string.target_range1_content2,
                    arrayOf(
                        D.Text(targetRange.toString()),
                        farthestTarget
                    )
                )
            }
            if (targetArea == 1) {
                D.Join(arrayOf(D.Format(R.string.target_front), rangeFarthestTarget))
            } else {
                rangeFarthestTarget
            }
        }
        9 -> D.Format(R.string.target_last_assignment_count1, arrayOf(getAssignmentCount()))
        10 -> D.Format(R.string.target_first_assignment_count1, arrayOf(getAssignmentCount()))
        11 -> D.Format(R.string.target_distance1, arrayOf(D.Text(actionValue1.toNumStr())))
        18 -> D.Format(R.string.target_all_summon_content1, arrayOf(getAssignmentSide()))
        //19 -> D.Unknown (tpReducing)
        20 -> D.Format(R.string.target_all_atk_content1, arrayOf(getAssignmentSide()))
        21 -> D.Format(R.string.target_all_magic_str_content1, arrayOf(getAssignmentSide()))
        //22 -> D.Unknown (allSummonRandom)
        23 -> D.Format(R.string.target_all_summon_of_self)
        24 -> D.Format(R.string.target_boss)
        //33 -> D.Unknown (shadow)
        //40 -> D.Unknown
        42 -> D.Format(R.string.target_multi_target1, arrayOf(getAssignmentSide()))
        in allMostTypes -> {
            @StringRes
            val content = when (targetType) {
                in hpRatioTypes -> R.string.hp_ratio
                in energyTypes -> R.string.energy
                in atkTypes -> R.string.atk
                in magicStrTypes -> R.string.magic_str
                else -> R.string.atk_or_magic_str//in atkOrMagicStrTypes
            }
            val mostTarget = D.Format(
                R.string.target_most_content1_extent2_assignment_count3,
                arrayOf(
                    D.Format(content),
                    D.Format(if (allMostHighTypes.contains(targetType)) R.string.target_high else R.string.target_low),
                    getAssignmentCount()
                )
            )
            val manyTarget = if (isFullRangeTarget()) {
                if (targetArea == 1) {
                    D.Join(
                        arrayOf(
                            D.Format(R.string.target_front_assignment1, arrayOf(getAssignment())),
                            mostTarget
                        )
                    )
                } else {
                    mostTarget
                }
            } else {
                val range = D.Text(targetRange.toString())
                D.Format(
                    R.string.target_range1_content2,
                    arrayOf(
                        if (targetArea == 1) {
                            D.Join(arrayOf(D.Format(R.string.target_front), range))
                        } else {
                            range
                        },
                        mostTarget
                    )
                )
            }
            if (withoutSelfTypes.contains(targetType)) {
                D.Join(arrayOf(D.Format(R.string.target_without_self), manyTarget))
            } else if (notInBackTypes.contains(targetType)) {
                D.Format(R.string.target_not_in_back_content1, arrayOf(manyTarget))
            } else {
                manyTarget
            }
        }
        else -> D.Unknown
    }
}

/**
 * when (targetAssignment) {
 *
 *    1 -> 敌人
 *
 *    2 -> 己方角色
 *
 *    else -> 敌人和己方角色
 *
 * }
 *
 * 敌人|己方角色|敌人和己方角色
 *
 * 敵|味方|敵と味方
 */
fun SkillAction.getAssignment(): D {
    return D.Format(
        when (targetAssignment) {
            1 -> R.string.target_enemy
            2 -> R.string.target_friendly
            else -> R.string.target_enemy_and_friendly
        }
    )
}

/**
 * when (targetAssignment) {
 *
 *    1 -> 敌人
 *
 *    2 -> 己方角色
 *
 *    else -> 敌人和己方角色
 *
 * }
 *
 * 敌人|己方角色|敌人和己方角色
 *
 * 敵１キャラ|味方１キャラ|敵と味方１キャラ
 */
fun SkillAction.getAssignmentOne(): D {
    return D.Format(
        when (targetAssignment) {
            1 -> R.string.target_enemy_one
            2 -> R.string.target_friendly_one
            else -> R.string.target_enemy_and_friendly_one
        }
    )
}

/**
 * when (targetAssignment) {
 *
 *    1 -> 敌方
 *
 *    2 -> 己方
 *
 *    else -> 敌方和己方
 *
 * }
 * 敌方|己方|敌方和己方
 *
 * 敵|味方|敵と味方
 */
fun SkillAction.getAssignmentSide(): D {
    return D.Format(
        when (targetAssignment) {
            1 -> R.string.target_enemy_side
            2 -> R.string.target_friendly_side
            else -> R.string.target_enemy_and_friendly_side
        }
    )
}

/**
 * when (targetAssignment) {
 *
 *    1 -> 敌人
 *
 *    2 -> 己方角色
 *
 *    else -> 敌人和己方角色
 *
 * }
 * {n}名{敌人|己方角色|敌人和己方角色}
 *
 * {敵|味方|敵と味方}{n}キャラ
 */
private fun SkillAction.getAssignmentCount(): D {
    return D.Format(
        when (targetAssignment) {
            1 -> R.string.target_enemy_count1
            2 -> R.string.target_friendly_count1
            else -> R.string.target_enemy_and_friendly_count1
        },
        arrayOf(
            D.Format(
                when (targetCount) {
                    1 -> R.string.target_count1
                    2 -> R.string.target_count2
                    3 -> R.string.target_count3
                    4 -> R.string.target_count4
                    else -> R.string.target_count5
                }
            )
        )
    )
}

/**
 * 第{n}
 *
 * {n}番目
 */
private fun SkillAction.getNumber(): D {
    val number = if (targetAssignment == 2) targetNumber - 1 else targetNumber
    return D.Format(
        when (number) {
            0 -> R.string.target_number1
            1 -> R.string.target_number2
            2 -> R.string.target_number3
            3 -> R.string.target_number4
            else -> R.string.target_number5
        }
    )
}

/**
 * if (actionType == 23 || actionType == 28) any_target else target
 *
 * 中的任意{0}
 *
 * の中の任意{0}
 */
private fun SkillAction.getAnyManyTarget(manyTarget: D): D {
    return if (isBranch()) {
        D.Join(
            arrayOf(
                manyTarget,
                D.Format(R.string.target_any_content1, arrayOf(getAssignment()))
            )
        )
    } else {
        manyTarget
    }
}

/**
 * if (targetType == 42) multi_target else target
 *
 * {0}的所有多目标部位
 *
 * {0}のマルチターゲット部位すべて
 */
private fun SkillAction.getDependMultiTarget(dependTarget: D): D {
    return if (targetType == 42) {
        D.Format(R.string.target_multi_target1, arrayOf(dependTarget))
    } else {
        dependTarget
    }
}

/**
 * targetRange <= 0 || targetRange >= 2160
 */
private fun SkillAction.isFullRangeTarget(): Boolean {
    return targetRange <= 0 || targetRange >= 2160
}

/** if (actionType == 23 || actionType == 28) any_target else target */
fun SkillAction.isBranch() = actionType in arrayOf(23, 28)
