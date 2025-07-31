package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect
import kotlin.math.ceil

fun SkillAction.getGiveValue(skillLevel: Int, actions: List<SkillAction>): D {
    /** actionDetail1：修饰的目标动作 */
    val targetAction = actions.find { it.actionId == actionDetail1 }!!

    /** 嵌套修饰的目标动作 */
    val nestTargetAction = if (targetAction.actionType == 26 || targetAction.actionType == 27) {
        actions.find { it.actionId == targetAction.actionDetail1 }!!
    } else {
        null
    }

    var giveValueCount = 1
    // 击杀数动作会叠算
    if (actionType == 26 && actionValue1 == 2.0) {
        var count = 0
        actions.forEach { action ->
            if (action.actionType == 26 && action.actionValue1 == 2.0) count++
        }
        giveValueCount = count
    }

    var isAdditive = true
    var value2 = actionValue2 * giveValueCount
    val value3 = actionValue3 * giveValueCount

    /** actionValue2, actionValue3 常量（如：(10 + 10 × 技能等级)） */
    val constantVariable = if (value3 == 0.0) {
        var isPercent = false
        if (targetAction.actionType == 1 && actionDetail2 == 6) {
            value2 *= 100
            isPercent = true
        } else if (targetAction.actionType == 10 && actionType != 27 && actionType != 74 && targetAction.isStatusPercent()) {
            isPercent = true
        } else if (targetAction.actionType == 98  && actionDetail2 == 1) {
            value2 *= 100
            isPercent = true
        }  else if (value2 < 0.0) {
            if (!(targetAction.actionType == 35 && actionDetail2 == 4)) {
                isAdditive = false
            }
            if (targetAction.actionType != 16 &&
                (targetAction.actionDetail1 == 1 || targetAction.actionDetail1 == 2) &&
                actionDetail2 == 1
            ) {
                value2 *= 100
                isPercent = true
            }
            value2 *= -1
        }
        D.Text(value2.toNumStr() + if (isPercent) "%" else "")
    } else {
        if (targetAction.actionType == 98  && actionDetail2 == 1) {
            val value = (value2 + value3 * skillLevel) * 100
            D.Text("${value.toNumStr()}%")
        } else if (value2 > 0.0 || value3 > 0.0) {
            D.Format(
                R.string.sub_formula_base1_lv2,
                arrayOf(
                    D.Text(value2.toNumStr()),
                    D.Text(value3.toNumStr())
                )
            )
        } else {
            isAdditive = false
            D.Format(
                R.string.sub_formula_base1_lv2,
                arrayOf(
                    D.Text((-value2).toNumStr()),
                    D.Text((-value3).toNumStr())
                )
            )
        }
    }

    val independentVariable = getGiveValueIndependentVariable()

    val content = getGiveValueContent(nestTargetAction ?: targetAction)

    val formula = if (nestTargetAction == null) {
        getGiveValueFormula(targetAction, constantVariable, independentVariable, null)
    } else {
        targetAction.getGiveValueFormula(
            nestTargetAction,
            constantVariable,
            independentVariable,
            targetAction.getGiveValueIndependentVariable()
        )
    }


    val maxValue = getMaxValue(skillLevel, targetAction)
    val maxIndependentVariable = getMaxIndependentVariable(skillLevel, targetAction)
    @StringRes
    val actionRes: Int
    @StringRes
    val maxRes: Int

    /** 26：加减，27：倍，74：{0}分之一 */
//    var type = actionType
//    if (type == 27 && targetAction.actionType == 1 && actionDetail2 == 3) {
//        type = 26
//    }

    when (actionType) {
        26 -> {
            if (isAdditive) {
                actionRes = R.string.action_additive_content1_formula2
                maxRes = R.string.action_additive_max1_content2_value3
            } else {
                actionRes = R.string.action_reduce_content1_formula2
                maxRes = R.string.action_reduce_max1_content2_value3
            }
        }
        27 -> {
            actionRes = R.string.action_multiply_content1_formula2
            maxRes = R.string.action_multiply_max1_content2_value3
        }
        else -> {// 74
            actionRes = R.string.action_divide_content1_formula2
            maxRes = R.string.action_divide_max1_content2_value3
        }
    }

    val result = D.Format(actionRes, arrayOf(content.style(underline = true), formula.style(primary = true)))
    return result.append(
        if (maxValue == null) D.Format(R.string.full_stop)
        else D.Format(
            maxRes,
            arrayOf(
                maxValue.style(primary = true, bold = true),
                independentVariable.style(primary = true),
                maxIndependentVariable.style(primary = true, bold = true)
            )
        )
    )
}

/**
 * actionValue1：自变量（如：敌人全体的数量）
 */
private fun SkillAction.getGiveValueIndependentVariable(): D {
    return when {
        actionValue1 > 2000.0 -> {
            D.Format(R.string.count_state1, arrayOf(getMarkContent((actionValue1 % 1000).toInt())))
        }
        actionValue1 > 100.0 -> {
            D.Format(R.string.count_state1, arrayOf(getMarkContent((actionValue1 % 100).toInt())))
        }
        actionValue1 > 20.0 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionValue1 % 10).toNumStr())))
            D.Format(R.string.count_state1, arrayOf(counter))
        }
        else -> {
            when (actionValue1.toInt()) {
                0 -> D.Format(R.string.hp_remnant)
                1 -> D.Format(R.string.hp_lost)
                2 -> D.Format(R.string.defeat_count)
                4 -> D.Format(R.string.count_target1, arrayOf(getTarget(depend)))
                5 -> D.Format(R.string.damaged_count)
                6 -> D.Format(R.string.damaged_amount)
                7 -> D.Format(R.string.of_s1_s2, arrayOf(getTarget(depend), D.Format(R.string.atk)))
                8 -> D.Format(R.string.of_s1_s2, arrayOf(getTarget(depend), D.Format(R.string.magic_str)))
                9 -> D.Format(R.string.of_s1_s2, arrayOf(getTarget(depend), D.Format(R.string.def)))
                10 -> D.Format(R.string.of_s1_s2, arrayOf(getTarget(depend), D.Format(R.string.magic_def)))
                12 -> D.Format(R.string.rear_friendly_count)
                13 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.hp_lost_ratio)))
                15 -> D.Format(R.string.hp_remnant_without_self_friendly)
                16 -> D.Format(R.string.energy_consumption_target1, arrayOf(getTarget(depend)))
                else -> D.Unknown
            }
        }
    }
}

/**
 * actionDetail2：修饰动作的类型（如：伤害的物理攻击力倍率）
 */
private fun SkillAction.getGiveValueContent(targetAction: SkillAction): D {
    return when (targetAction.actionType) {
        1 -> when (actionDetail2) {
            3 -> D.Format(R.string.give_damage_rate1, arrayOf(getAtkType(targetAction.actionDetail1)))
            6 -> D.Format(R.string.give_critical_damage)
            7 -> D.Format(R.string.give_disregard_def_type1, arrayOf(getDamageType(targetAction.actionDetail1)))
            else -> D.Format(R.string.give_damage)
        }
        3 -> D.Format(R.string.give_distance)
        4 -> D.Format(R.string.give_hp_recovery)
        6 -> if (actionDetail2 == 3) D.Format(R.string.give_time)
        else D.Format(
            when (targetAction.actionDetail1) {
                1, 2, 5 -> R.string.give_barrier_guard
                else -> R.string.give_barrier_drain
            }
        )
        8 -> if ((targetAction.actionDetail1 == 1 || targetAction.actionDetail1 == 2) &&
            actionDetail2 == 1) D.Format(R.string.give_speed)
        else D.Format(R.string.give_time)
        9 -> if (actionDetail2 == 3) D.Format(R.string.give_time)
        else D.Join(
            arrayOf(
                getAbnormalDamageContent(targetAction.actionDetail1),
                D.Format(R.string.give_damage)
            )
        )
        10 -> if (actionDetail2 == 4) D.Format(R.string.give_time)
        else D.Format(
            if (targetAction.isStatusUp()) R.string.give_up_amount_content1
            else R.string.give_down_amount_content1,
            arrayOf(getStatusContent(targetAction.actionDetail1 / 10))
        )
        16 -> D.Format(
            if (targetAction.actionDetail1 == 1 || targetAction.actionDetail1 == 4) R.string.give_energy_recovery
            else R.string.give_energy_decrement
        )
        35 -> when (actionDetail2) {
            4 -> D.Format(
                if (actionValue2 < 0.0) R.string.give_mark_consume_state1 else R.string.give_mark_add_state1,
                arrayOf(getMarkContent(targetAction.actionValue2.toInt()))
            )
            3 -> D.Format(R.string.give_time)
            1 -> D.Format(
                R.string.give_mark_max_state1,
                arrayOf(getMarkContent(targetAction.actionValue2.toInt()))
            )
            else -> D.Unknown
        }
        36 -> when (actionDetail2) {
            5 -> D.Format(R.string.give_time)
            else -> D.Format(R.string.give_dot_damage)
        }
        37 -> when (actionDetail2) {
            5 -> D.Format(R.string.give_time)
            else -> D.Format(R.string.give_hp_regeneration)
        }
        38 -> if (actionDetail2 == 3) D.Format(R.string.give_time)
        else D.Format(
            if (targetAction.actionDetail1 % 10 == 0) R.string.give_up_amount_content1
            else R.string.give_down_amount_content1,
            arrayOf(getStatusContent(targetAction.actionDetail1 / 10))
        )
        46 -> D.Format(R.string.give_damage)
        48 -> if (actionDetail2 == 5) D.Format(R.string.give_time)
        else D.Format(
            if (targetAction.actionDetail2 == 1) R.string.give_hp_regeneration
            else R.string.give_energy_regeneration
        )
        59 -> if (actionDetail2 == 1) D.Format(R.string.give_hp_recovery_down)
        else D.Format(R.string.give_time)
        98 -> if (actionDetail2 == 1) D.Format(R.string.give_energy_cut_effect)
        else D.Format(R.string.give_time)
        else -> D.Unknown
    }
}

/**
 * 公式（如：{ 物理攻击力 * 敌人全体的数量 }）
 */
private fun SkillAction.getGiveValueFormula(
    targetAction: SkillAction,
    constantVariable: D,
    independentVariable: D,
    nestIndependentVariable: D?
): D {
    val otherConstantVariable = when (targetAction.actionType) {
        1 -> when (actionDetail2) {
            1, 3, 6, 7 -> null
            2, 4 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        3 -> null
        4 -> when (actionDetail2) {
            2 -> null
            3 -> D.Format(R.string.skill_level)
            4 -> getAtkType(targetAction.actionDetail1)
            else -> D.Unknown
        }
        6, 9, 38 -> when (actionDetail2) {
            1, 3 -> null
            2 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        8, 16, 35 -> null
        10 -> when (actionDetail2) {
            2, 4 -> null
            3 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        36, 37 -> when (actionDetail2) {
            1, 3, 7 -> null
            2, 4, 6 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        46 -> when (actionDetail2) {
            1 -> null
            2 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        48 -> when (actionDetail2) {
            1, 5 -> null
            2 -> D.Format(R.string.skill_level)
            3 -> getAtkType(targetAction.actionDetail1)
            else -> D.Unknown
        }
        59 -> null
        98 -> null
        else -> D.Unknown
    }
    val nonNullElements = listOfNotNull(
        constantVariable, otherConstantVariable, independentVariable, nestIndependentVariable
    ).toTypedArray()
    val result = D.Format(
        when (nonNullElements.size) {
            2 -> R.string.formula_m1_m2
            3 -> R.string.formula_m1_m2_m3
            else -> R.string.formula_m1_m2_m3_m4
        },
        nonNullElements
    )
    return if (targetAction.actionType == 46) {
        D.Format(
            if (targetAction.actionDetail1 == 2) R.string.content_hp_ratio1
            else R.string.content_max_hp_ratio1,
            arrayOf(result.append(D.Text("%")))
        )
    } else {
        result
    }
}

/** actionValue4, actionValue5：上限值 */
private fun SkillAction.getMaxValue(skillLevel: Int, targetAction: SkillAction): D? {
    return if (actionValue4 == 0.0 && actionValue5 == 0.0) {
        null
    } else {
        if (actionValue5 == 0.0) {
            if (targetAction.actionType == 1 && actionDetail2 == 6) {
                D.Text("${(actionValue4 * 100).toNumStr()}%")
            } else if (
                (targetAction.actionType == 10 && targetAction.isStatusPercent()) ||
                targetAction.actionType == 46
            ) {
                D.Text("${actionValue4.toNumStr()}%")
            } else if (targetAction.actionType == 35 && actionDetail2 == 4 && actionValue2 < 0.0) {
                D.Text((-actionValue4).toNumStr())
            } else {
                D.Text(actionValue4.toNumStr())
            }
        } else {
            if (actionValue4 > 0.0 && actionValue5 > 0.0) {
                D.Text(ceil(actionValue4 + actionValue5 * skillLevel).toNumStr())// TODO 不确定的取整方式
            } else {
                D.Text(ceil((-actionValue4) + (-actionValue5) * skillLevel).toNumStr())// TODO 不确定的取整方式
            }
        }
    }
}

private fun SkillAction.getMaxIndependentVariable(skillLevel: Int, targetAction: SkillAction): D {
    var otherVariable: D? = null
    val max = actionValue4 + actionValue5 * skillLevel
    val constantVariable = actionValue2 + actionValue3 * skillLevel
    val otherConstantVariable: Double = when (targetAction.actionType) {
        1 -> when (actionDetail2) {
            1, 3, 6, 7 -> 1.0
            2, 4 -> skillLevel.toDouble()
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        3 -> 1.0
        4 -> when (actionDetail2) {
            2 -> 1.0
            3 -> skillLevel.toDouble()
            4 -> {
                otherVariable = getAtkType(targetAction.actionDetail1)
                1.0
            }

            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        6, 9, 38 -> when (actionDetail2) {
            1, 3 -> 1.0
            2 -> skillLevel.toDouble()
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        8, 16, 35 -> 1.0
        10 -> when (actionDetail2) {
            2, 4 -> 1.0
            3 -> skillLevel.toDouble()
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        36, 37 -> when (actionDetail2) {
            1, 3, 7 -> 1.0
            2, 4, 6 -> skillLevel.toDouble()
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        46 -> when (actionDetail2) {
            1 -> 1.0
            2 -> skillLevel.toDouble()
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        48 -> when (actionDetail2) {
            1, 5 -> 1.0
            2 -> skillLevel.toDouble()
            3 -> {
                otherVariable = getAtkType(targetAction.actionDetail1)
                1.0
            }
            else -> {
                otherVariable = D.Unknown
                1.0
            }
        }
        59 -> 1.0
        98 -> 1.0
        else -> {
            otherVariable = D.Unknown
            1.0
        }
    }
    val maxIndependentVariable = ceil(max / constantVariable / otherConstantVariable)
    return if (otherVariable == null) {
        D.Text(maxIndependentVariable.toNumStr())
    } else {
        D.Text("${maxIndependentVariable.toNumStr()} / ").append(otherVariable)
    }
}

fun SkillAction.getGiveValueEffect(skillLevel: Int, actions: List<SkillAction>): SkillEffect {
    val targetAction = actions.find { it.actionId == actionDetail1 }
    if (targetAction != null) {
        if (targetAction.actionType == 98) {
            return targetAction.getEnergyCutEffect(actionValue2 + actionValue3 * skillLevel)
        }
    }
    return getUnknownEffect()
}
