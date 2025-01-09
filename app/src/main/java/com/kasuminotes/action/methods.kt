package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.MainApplication
import com.kasuminotes.R
import com.kasuminotes.common.Label
import com.kasuminotes.common.Language
import com.kasuminotes.data.Property
import com.kasuminotes.ui.app.languageSP
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

private fun getString(type: String, id: String): String? {
    val stringsMap = MainApplication.strings
    return if (stringsMap != null) {
        val lang = when (MainApplication.context.languageSP) {
            Locale.JAPANESE.language -> Language.JP.name
            else -> Language.CN.name
        }
        stringsMap.getOrDefault(lang, null)
            ?.getOrDefault(type, null)
            ?.getOrDefault(id, null)
    } else {
        null
    }
}

fun getAbnormalContent(detail: Int): D {
    val abnormal: String? = getString("abnormal", detail.toString())
    return if (abnormal == null) {
        D.Format(R.string.abnormal_unknown, arrayOf(D.Text(detail.toString())))
    } else {
        D.Text(abnormal)
    }
}

fun getAbnormalDamageContent(detail: Int): D {
    val abnormalDamage = getString("dot", detail.toString())
    return if (abnormalDamage == null) {
        D.Format(R.string.dot_unknown, arrayOf(D.Text(detail.toString())))
    } else {
        D.Text(abnormalDamage)
    }
}

fun getMarkContent(detail: Int): D {
    val mark = getString("mark",detail.toString())
    return if (mark == null) {
        D.Format(R.string.mark_unknown, arrayOf(D.Text(detail.toString())))
    } else {
        D.Text(mark)
    }
}

fun getUnitName(id: Int): D {
    val unitName = getString("summon", id.toString())
    return if (unitName == null) {
        D.Format(R.string.summon_unknown, arrayOf(D.Text(id.toString())))
    } else {
        D.Text(unitName)
    }
}

fun isAtkType(detail: Int): Boolean = detail == 1 || detail == 3

fun getAtkType(detail: Int) = D.Format(if (isAtkType(detail)) R.string.atk else R.string.magic_str)

fun getDefType(detail: Int) = D.Format(if (isAtkType(detail)) R.string.def else R.string.magic_def)

fun getDamageType(detail: Int) = D.Format(if (isAtkType(detail)) R.string.physical else R.string.magic)

fun getBaseLvAtkFormula(
    detail: Int,
    base: Double,
    lvCoefficient: Double,
    atkCoefficient: Double,
    plusAtkCoefficient: Double,
    skillLevel: Int,
    property: Property,
    callback: (Double) -> Double = { floor(it) }// TODO 不确定的取整方式
): D {
    @StringRes
    val atkType: Int
    val atk: Double
    if (isAtkType(detail)) {
        atkType = R.string.atk
        atk = property.atk
    } else {
        atkType = R.string.magic_str
        atk = property.magicStr
    }
    val result = callback(base + lvCoefficient * skillLevel + (atkCoefficient + plusAtkCoefficient * skillLevel) * atk)
    val formula = if (base == 0.0 && lvCoefficient == 0.0) {
        if (plusAtkCoefficient == 0.0) {
            D.Format(
                R.string.formula_atk1_type2_result3,
                arrayOf(
                    D.Text(atkCoefficient.toNumStr()).style(bold = true),
                    D.Format(atkType),
                    D.Text(result.toNumStr()).style(bold = true)
                )
            )
        } else {
            D.Format(
                R.string.formula_atk1_plus2_type3_result4,
                arrayOf(
                    D.Text(atkCoefficient.toNumStr()).style(bold = true),
                    D.Text(plusAtkCoefficient.toString()),
                    D.Format(atkType),
                    D.Text(result.toNumStr()).style(bold = true)
                )
            )
        }
    } else if (plusAtkCoefficient == 0.0) {
        if (atkCoefficient== 0.0 && lvCoefficient == 0.0) {
            D.Text(result.toNumStr()).style(bold = true)
        } else {
            D.Format(
                R.string.formula_base1_lv2_atk3_type4_result5,
                arrayOf(
                    D.Text(base.toNumStr()),
                    D.Text(lvCoefficient.toNumStr()),
                    D.Text(atkCoefficient.toNumStr()).style(bold = true),
                    D.Format(atkType),
                    D.Text(result.toNumStr()).style(bold = true)
                )
            )
        }
    } else {
        D.Format(
            R.string.formula_base1_lv2_atk3_plus4_type5_result6,
            arrayOf(
                D.Text(base.toNumStr()),
                D.Text(lvCoefficient.toNumStr()),
                D.Text(atkCoefficient.toNumStr()).style(bold = true),
                D.Text(plusAtkCoefficient.toNumStr()),
                D.Format(atkType),
                D.Text(result.toNumStr())
            )
        )
    }
    return formula.style(primary = true)
}

fun getBaseLvFormula(
    base: Double,
    lvCoefficient: Double,
    skillLevel: Int,
    callback: (Double) -> Double = { ceil(it) }// TODO 不确定的取整方式
): D {
    val formula = if (lvCoefficient == 0.0) {
        D.Text(base.toNumStr()).style(bold = true)
    } else {
        val result = callback(base + lvCoefficient * skillLevel)
        if (base == 0.0) {
            D.Format(
                R.string.formula_lv1_result2,
                arrayOf(
                    D.Text(lvCoefficient.toNumStr()).style(bold = true),
                    D.Text(result.toNumStr()).style(bold = true)
                )
            )
        } else {
            D.Format(
                R.string.formula_base1_lv2_result3,
                arrayOf(
                    D.Text(base.toNumStr()),
                    D.Text(lvCoefficient.toNumStr()).style(bold = true),
                    D.Text(result.toNumStr()).style(bold = true)
                )
            )
        }
    }
    return formula.style(primary = true)
}

fun getStatusContent(detail: Int): D {
    return when (detail) {
        0 -> D.Format(R.string.max_hp)
        1 -> D.Format(R.string.atk)
        2 -> D.Format(R.string.def)
        3 -> D.Format(R.string.magic_str)
        4 -> D.Format(R.string.magic_def)
        5 -> D.Format(R.string.dodge)
        6 -> D.Format(R.string.physical_critical)
        7 -> D.Format(R.string.magic_critical)
        8 -> D.Format(R.string.energy_recovery_rate)
        9 -> D.Format(R.string.life_steal)
        10 -> D.Format(R.string.move_speed)
        11 -> D.Format(R.string.physical_critical_damage)
        12 -> D.Format(R.string.magic_critical_damage)
        13 -> D.Format(R.string.accuracy)
        14 -> D.Format(R.string.received_critical_damage)
        15 -> D.Format(R.string.received_damage)
        16 -> D.Format(R.string.received_physical_damage)
        17 -> D.Format(R.string.received_magic_damage)
        18 -> D.Format(R.string.physical_damage)
        19 -> D.Format(R.string.magic_damage)
        else -> D.Unknown
    }
}

fun getStatusIndex(detail: Int): Int? {
    return when (detail) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 4
        5 -> 7
        6 -> 5
        7 -> 6
        8 -> 13
        9 -> 8
        13 -> 16
        else -> null
    }
}

fun getSkillLabel(detail: Int): D {
    return D.Text(
        when (val value = detail / 100 % 10) {
            1 -> Label.ub
            else -> Label.skill + (value - 1).toString()
        }
    )
}

fun Double.toNumStr(): String {
    val nf = NumberFormat.getInstance()
    nf.isGroupingUsed = false
    nf.maximumFractionDigits = 9
    return nf.format(this)
}
