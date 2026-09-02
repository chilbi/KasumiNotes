package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillEffect
import kotlin.math.ceil

fun SkillAction.getGiveValue(skillLevel: Int, actions: List<SkillAction>): D {
    /** 嵌套修饰的目标动作 */
//    val nestTargetAction = if (targetAction.actionType == 26 || targetAction.actionType == 27) {
//        actions.find { it.actionId == targetAction.actionDetail1 }!!
//    } else {
//        null
//    }

//    var giveValueCount = 1
//    // 击杀数动作会叠算
//    if (actionType == 26 && actionValue1 == 2.0) {
//        var count = 0
//        actions.forEach { action ->
//            if (action.actionType == 26 && action.actionValue1 == 2.0) count++
//        }
//        giveValueCount = count
//    }

    /** actionDetail1：修饰的目标动作 */
    val targetAction = actions.find { it.actionId == actionDetail1 }
    if (targetAction == null) {
        return D.Text("")
    }

    // actionDetail2：修饰动作的类型（如：伤害的物理攻击力倍率）
    val content = getGiveValueContent(targetAction)

    // actionValue1：自变量（如：敌人全体的数量）
    val independentVariable = getGiveValueIndependentVariable()

    // actionValue2, actionValue3 常量（如：(10 + 10 × 技能等级)）
    val giveValueFormat = getGiveValueFormat(targetAction, skillLevel)

    // actionValue4, actionValue5 上限值
    val maxValue = getMaxValue(targetAction, skillLevel, giveValueFormat)

    // 达到上限值时的自变量值
    val maxIndependentVariable = getMaxIndependentVariable(targetAction, skillLevel)

    // 公式（如：{ 物理攻击力 * 敌人全体的数量 }）
    val formula = getGiveValueFormula(targetAction, giveValueFormat.constantVariable, independentVariable, null)

    @StringRes
    val actionRes: Int
    @StringRes
    val maxRes: Int

    when (actionType) {
        26 -> {
            if (giveValueFormat.isAdditive) {
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
fun SkillAction.getGiveValueIndependentVariable(): D {
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
                17 -> D.Format(R.string.debuff_types_count_target1, arrayOf(getTarget(depend)))
                else -> D.Unknown
            }
        }
    }
}

private data class GiveValueFormat(
    val constantVariable: D,
    val isAdditive: Boolean,
    val isPercent: Boolean,
    val shouldMultiplyBy100: Boolean
)

/** actionValue2, actionValue3 常量（如：(10 + 10 × 技能等级)） */
private fun SkillAction.getGiveValueFormat(targetAction: SkillAction, skillLevel: Int): GiveValueFormat {
    var isAdditive = true
    var isPercent = false
    var shouldMultiplyBy100 = false
    var value2 = actionValue2// * giveValueCount
    var value3 = actionValue3// * giveValueCount

    if ((value2 + value3 * skillLevel) < 0.0) {
        isAdditive = false
    }

    if (targetAction.actionType == 1 && actionDetail2 == 6) {
        isPercent = true
        shouldMultiplyBy100 = true
    } else if (targetAction.actionType == 4 && targetAction.actionValue1 != 1.0) {
        isPercent = true
    } else if (targetAction.actionType == 10 &&
        actionType != 27 &&
        actionType != 74 &&
        targetAction.isStatusPercent()
    ) {
        isPercent = true
    } else if (targetAction.actionType == 46) {
        isPercent = true
    } else if (targetAction.actionType == 72 &&
        !(targetAction.actionDetail1 == 4 || targetAction.actionDetail1 == 5)
    ) {
        isPercent = true
    } else if (targetAction.actionType == 98 && actionDetail2 == 1) {
        isPercent = true
        if (targetAction.actionDetail1 == 0) {
            shouldMultiplyBy100 = true
        }
    } else if (targetAction.actionType == 110 && actionDetail2 == 1) {
        isPercent = true
    } else if (value3 == 0.0 &&//不明
        value2 < 0.0 &&
        targetAction.actionType != 16 &&
        (targetAction.actionDetail1 == 1 || targetAction.actionDetail1 == 2) &&
        actionDetail2 == 1
    ) {
        isPercent = true
        shouldMultiplyBy100 = true
    }

    if (!isAdditive) {
        value2 = -value2
        value3 = -value3
    }

    if (shouldMultiplyBy100) {
        value2 *= 100
        value3 *= 100
    }

    val formula: D = if (value3 == 0.0) {
        D.Text(value2.toNumStr())
    } else {
        D.Format(
            R.string.sub_formula_base1_lv2,
            arrayOf(
                D.Text(value2.toNumStr()),
                D.Text(value3.toNumStr())
            )
        )
    }

    val constantVariable = if (isPercent) {
        formula.append(D.Text("%"))
    } else {
        formula
    }

    return GiveValueFormat(
        constantVariable,
        isAdditive,
        isPercent,
        shouldMultiplyBy100
    )
}

/**
 * actionDetail2：修饰动作的类型（如：伤害的物理攻击力倍率）
 */
private fun SkillAction.getGiveValueContent(targetAction: SkillAction): D {
    return when (targetAction.actionType) {
        1 -> when (actionDetail2) {
            3 -> D.Format(R.string.give_damage_rate1, arrayOf(getAtkType(targetAction.actionDetail1)))
            5 -> D.Format(R.string.give_critical1, arrayOf(D.Text(targetAction.actionValue5.toNumStr())))
            6 -> D.Format(R.string.give_critical_damage)
            7 -> D.Format(R.string.give_disregard_def_type1, arrayOf(getDamageType(targetAction.actionDetail1)))
            else -> D.Format(R.string.give_damage)
        }
        3 -> D.Format(R.string.give_distance)
        4 -> when (actionDetail2) {
            4 -> D.Format(R.string.give_hp_recovery_rate1, arrayOf(getAtkType(targetAction.actionDetail1)))
            else ->D.Format(R.string.give_hp_recovery)
        }
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
        21 -> when (actionDetail2) {
            1, 2 -> D.Format(R.string.give_time)
            else -> D.Unknown
        }
        26, 27, 74 -> when (actionDetail2) {
            2 -> D.Format(R.string.give_base_value1, arrayOf(D.Text(targetAction.actionValue2.toNumStr())))
            else -> D.Unknown
        }
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
        72 -> when (actionDetail2) {
            1, 2 -> D.Format(R.string.give_damage_cut1, arrayOf(targetAction.getDamageCutContent()))
            3 -> D.Format(R.string.give_time)
            else -> D.Unknown
        }
        98 -> if (actionDetail2 == 1) D.Format(
            if (targetAction.actionDetail1 == 0) R.string.give_energy_down_cut
            else R.string.give_energy_down_limit
        ) else D.Format(R.string.give_time)
        110 -> when (actionDetail2) {
            1 -> D.Format(R.string.give_damage_up)
            2 -> D.Format(R.string.give_time)
            7 -> D.Format(R.string.give_max_damage_up)
            else -> D.Unknown
        }
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
            1, 3, 5, 6, 7 -> null
            2, 4 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        3 -> null
        4 -> when (actionDetail2) {
            2, 4 -> null
            3 -> D.Format(R.string.skill_level)
//            4 -> getAtkType(targetAction.actionDetail1)
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
        21 -> when (actionDetail2) {
            1 -> null
            2 -> D.Format(R.string.skill_level)
            else -> D.Unknown
        }
        26, 27, 74 -> null
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
        59, 72, 98, 110 -> null
        else -> D.Unknown
    }
    val nonNullElements = listOfNotNull(
        constantVariable, otherConstantVariable, independentVariable, nestIndependentVariable
    ).toTypedArray()
    return D.Format(
        when (nonNullElements.size) {
            2 -> R.string.formula_m1_m2
            3 -> R.string.formula_m1_m2_m3
            else -> R.string.formula_m1_m2_m3_m4
        },
        nonNullElements
    )
//    return if (targetAction.actionType == 46) {
//        D.Format(
//            if (targetAction.actionDetail1 == 2) R.string.content_hp_ratio1
//            else R.string.content_max_hp_ratio1,
//            arrayOf(result.append(D.Text("%")))
//        )
//    } else {
//        result
//    }
}

private fun SkillAction.getDependSkillLevel(targetAction: SkillAction, skillLevel: Int): Int {
    return when (targetAction.actionType) {
        1 -> when (actionDetail2) {
            2, 4 -> skillLevel
            else -> 1
        }
        3 -> 1
        4 -> when (actionDetail2) {
            3 -> skillLevel
            else -> 1
        }
        6, 9, 38 -> when (actionDetail2) {
            2 -> skillLevel
            else -> 1
        }
        8, 16, 35 -> 1
        10 -> when (actionDetail2) {
            3 -> skillLevel
            else -> 1
        }
        21 -> when (actionDetail2) {
            2 -> skillLevel
            else -> 1
        }
        26, 27, 74 -> 1
        36, 37 -> when (actionDetail2) {
            2, 4, 6 -> skillLevel
            else -> 1
        }
        46 -> when (actionDetail2) {
            2 -> skillLevel
            else -> 1
        }
        48 -> when (actionDetail2) {
            2 -> skillLevel
            else -> 1
        }
        59 -> 1
        72 -> when (actionDetail2) {
            2 -> skillLevel
            else -> 1
        }
        98 -> 1
        else -> 1
    }
}

/** actionValue4, actionValue5：上限值 */
private fun SkillAction.getMaxValue(
    targetAction: SkillAction,
    skillLevel: Int,
    giveValueFormat: GiveValueFormat
): D? {
    val dependSkillLevel = getDependSkillLevel(targetAction, skillLevel)
    return if (actionValue4 == 0.0 && actionValue5 == 0.0) {
        null
    } else {
        var maxValue = (actionValue4 + actionValue5 * skillLevel) * dependSkillLevel
        if (!giveValueFormat.isAdditive ||
            (targetAction.actionType == 35 && actionDetail2 == 4 && actionValue2 < 0.0)//不明
        ) {
            maxValue = -maxValue
        }
        if (giveValueFormat.shouldMultiplyBy100) {
            maxValue *= 100
        }
        if (giveValueFormat.isPercent) {
            D.Text("${maxValue.toNumStr()}%")
        } else {
            D.Text(maxValue.toNumStr())
        }
    }
}

/** 达到上限值时的自变量值 */
private fun SkillAction.getMaxIndependentVariable(targetAction: SkillAction, skillLevel: Int): D {
//    var otherVariable: D? = null
    val max = actionValue4 + actionValue5 * skillLevel * getDependSkillLevel(targetAction, skillLevel)
    val constantVariable = actionValue2 + actionValue3 * skillLevel
    return D.Text((ceil(max / constantVariable * 10000.0) / 10000.0).toNumStr())
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
