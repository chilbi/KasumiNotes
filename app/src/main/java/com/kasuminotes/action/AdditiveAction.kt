package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.SkillAction
import kotlin.math.ceil

fun SkillAction.getAdditive(skillLevel: Int, actions: List<SkillAction>): D {
    val modifyAction = actions.find { it.actionId == actionDetail1 }!!
    var isAdd = true

    val max = if (actionValue4 == 0.0 && actionValue5 == 0.0) {
        null
    } else {
        if (actionValue5 == 0.0) {
            if (modifyAction.actionType == 1 && actionDetail2 == 6) {
                D.Text("${(actionValue4 * 100).toNumStr()}%")
            } else if (modifyAction.actionType == 10 && (modifyAction.actionDetail1 == 141 || modifyAction.actionValue1 == 2.0)) {
                D.Text("${actionValue4.toNumStr()}%")
            } else if (modifyAction.actionType == 35 && actionDetail2 == 4 && actionValue2 < 0.0) {
                D.Text((-actionValue4).toNumStr())
            } else {
                D.Text(actionValue4.toNumStr())
            }
        } else {
            if (actionValue4 > 0.0 && actionValue5 > 0.0) {
                D.Text(ceil(actionValue4 + actionValue5 * skillLevel).toNumStr())
            } else {
                D.Text(ceil((-actionValue4) + (-actionValue5) * skillLevel).toNumStr())
            }
        }
    }

    val factor = if (actionValue3 == 0.0) {
        if (modifyAction.actionType == 1 && actionDetail2 == 6) {
            D.Text("${(actionValue2 * 100).toNumStr()}%")
        } else if (modifyAction.actionType == 10 && (modifyAction.actionDetail1 == 141 || modifyAction.actionValue1 == 2.0)) {
            D.Text("${actionValue2.toNumStr()}%")
        } else if (modifyAction.actionType == 35 && actionDetail2 == 4 && actionValue2 < 0.0) {
            D.Text((-actionValue2).toNumStr())
        } else {
            D.Text(actionValue2.toNumStr())
        }
    } else {
        if (actionValue2 > 0.0 && actionValue3 > 0.0) {
            D.Format(
                R.string.sub_formula_base1_lv2,
                arrayOf(
                    D.Text(actionValue2.toNumStr()),
                    D.Text(actionValue3.toNumStr())
                )
            )
        } else {
            isAdd = false
            D.Format(
                R.string.sub_formula_base1_lv2,
                arrayOf(
                    D.Text((-actionValue2).toNumStr()),
                    D.Text((-actionValue3).toNumStr())
                )
            )
        }
    }

    val coefficient = when {
        actionValue1 > 100.0 -> {
            D.Format(R.string.count_state1, arrayOf(getStateContent((actionValue1 % 100).toInt())))
        }
        actionValue1 > 20.0 -> {
            val counter = D.Format(R.string.counter_num1, arrayOf(D.Text((actionValue1 % 10).toNumStr())))
            D.Format(R.string.count_state1, arrayOf(counter))
        }
        else -> {
            when (actionValue1.toInt()) {
                0 -> D.Format(R.string.hp_remanent)
                1 -> D.Format(R.string.hp_lost)
                2 -> D.Format(R.string.overthrow_count)
                4 -> D.Format(R.string.count_target1, arrayOf(getTarget(depend)))
                5 -> D.Format(R.string.damaged_count)
                6 -> D.Format(R.string.damaged_amount)
                7 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.of), D.Format(R.string.atk)))
                8 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.of), D.Format(R.string.magic_str)))
                9 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.of), D.Format(R.string.def)))
                10 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.of), D.Format(R.string.magic_def)))
                12 -> D.Format(R.string.rear_friendly_count)
                13 -> D.Join(arrayOf(getTarget(depend), D.Format(R.string.hp_lost_ratio)))
                else -> D.Unknown
            }
        }
    }

    val content = when (modifyAction.actionType) {
        1 -> D.Format(
            if (actionDetail2 == 6) R.string.additive_critical_damage
            else R.string.additive_damage
        )
        4 -> D.Format(R.string.additive_hp_recovery)
        6 -> if (actionDetail2 == 3) D.Format(R.string.additive_time)
        else D.Format(
            when (modifyAction.actionDetail1) {
                1, 2, 5 -> R.string.additive_barrier_guard
                else -> R.string.additive_barrier_drain
            }
        )
        8 -> D.Format(R.string.additive_time)
        9 -> if (actionDetail2 == 3) D.Format(R.string.additive_time)
        else D.Join(
            arrayOf(
                getAbnormalDamageContent(modifyAction.actionDetail1),
                D.Format(R.string.additive_damage)
            )
        )
        10 -> if (actionDetail2 == 4) D.Format(R.string.additive_time)
        else D.Format(
            if (modifyAction.actionDetail1 == 141 || modifyAction.actionDetail1 % 10 == 0) R.string.additive_up_amount_content1
            else R.string.additive_down_amount_content1,
            arrayOf(getStatusContent(modifyAction.actionDetail1 / 10))
        )
        16 -> D.Format(R.string.additive_energy_recovery)
        35 -> when (actionDetail2) {
            4 -> D.Format(
                if (actionValue2 > 0.0) R.string.additive_mark_add_state1 else R.string.additive_mark_consume_state1,
                arrayOf(getStateContent(modifyAction.actionValue2.toInt()))
            )
            3 -> D.Format(R.string.additive_time)
            1 -> D.Format(
                R.string.additive_mark_max_state1,
                arrayOf(getStateContent(modifyAction.actionValue2.toInt()))
            )
            else -> D.Unknown
        }
        38 -> if (actionDetail2 == 3) D.Format(R.string.additive_time)
        else D.Format(
            if (modifyAction.actionDetail1 % 10 == 0) R.string.additive_up_amount_content1
            else R.string.additive_down_amount_content1,
            arrayOf(getStatusContent(modifyAction.actionDetail1 / 10))
        )
        48 -> if (actionDetail2 == 5) D.Format(R.string.additive_time)
        else D.Format(
            if (modifyAction.actionDetail2 == 1) R.string.additive_hp_regeneration
            else R.string.additive_energy_regeneration
        )
        else -> D.Unknown
    }

    val formula = when (modifyAction.actionType) {
        1 -> when (actionDetail2) {
            1, 6 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
            2 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
            3 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, getAtkType(modifyAction.actionDetail1), coefficient))
            else -> D.Unknown
        }
        4 -> when (actionDetail2) {
            2 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
            3 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
            4 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, getAtkType(modifyAction.actionDetail1), coefficient))
            else -> D.Unknown
        }
        6, 9, 38 -> when (actionDetail2) {
            1, 3 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
            2 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
            else -> D.Unknown
        }
        8, 35 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
//        9 -> when (actionDetail2) {
//            1, 3 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
//            2 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
//            else -> D.Unknown
//        }
        10 -> when (actionDetail2) {
            2, 4 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
            3 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
            else -> D.Unknown
        }
        16 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
//        38 -> when (actionDetail2) {
//            1, 3 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
//            2 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
//            else -> D.Unknown
//        }
        48 -> when (actionDetail2) {
            1, 5 -> D.Format(R.string.formula_m1_m2, arrayOf(factor, coefficient))
            2 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, D.Format(R.string.skill_level), coefficient))
            3 -> D.Format(R.string.formula_m1_m2_m3, arrayOf(factor, getAtkType(modifyAction.actionDetail1), coefficient))
            else -> D.Unknown
        }
        else -> D.Unknown
    }

    @StringRes
    val actionRes: Int
    @StringRes
    var maxRes: Int? = null

    if (actionType == 26) {
        if (isAdd) {
            actionRes = R.string.action_additive_content1_formula2
            if (max != null) {
                maxRes = R.string.action_additive_max1
            }
        } else {
            actionRes = R.string.action_additive_reduce_content1_formula2
            if (max != null) {
                maxRes = R.string.action_additive_reduce_max1
            }
        }
    } else {
        if (isAdd) {
            actionRes = R.string.action_multiple_content1_formula2
            if (max != null) {
                maxRes = R.string.action_multiple_max1
            }
        } else {
            actionRes = R.string.action_multiple_reduce_content1_formula2
            if (max != null) {
                maxRes = R.string.action_multiple_reduce_max1
            }
        }
    }

    return if (maxRes == null) {
        D.Format(actionRes, arrayOf(content, formula))
    } else{
        D.Join(
            arrayOf(
                D.Format(actionRes, arrayOf(content, formula)),
                D.Format(maxRes, arrayOf(max!!))
            )
        )
    }
}
