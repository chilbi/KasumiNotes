package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.data.Property
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.floor

fun getBaseLvAtkFormula(
    detail: Int,
    base: Double,
    lvCoefficient: Double,
    atkCoefficient: Double,
    skillLevel: Int,
    property: Property,
    callback: (Double) -> Double = { result -> floor(result) }
): D {
    @StringRes
    val atkType: Int
    val atk: Double
    if (detail == 1 || detail == 3) {
        atkType = R.string.atk
        atk = property.atk
    } else {
        atkType = R.string.magic_str
        atk = property.magicStr
    }
    val result = callback(base + lvCoefficient * skillLevel + atkCoefficient * atk)
    return if (base == 0.0 && lvCoefficient == 0.0) {
        D.Format(
            R.string.formula_atk1_type2_result3,
            arrayOf(
                D.Text(atkCoefficient.toNumStr()),
                D.Format(atkType),
                D.Text(result.toNumStr())
            )
        )
    } else {
        D.Format(
            R.string.formula_base1_lv2_atk3_type4_result5,
            arrayOf(
                D.Text(base.toNumStr()),
                D.Text(lvCoefficient.toNumStr()),
                D.Text(atkCoefficient.toNumStr()),
                D.Format(atkType),
                D.Text(result.toNumStr())
            )
        )
    }
}

fun getBaseLvFormula(
    base: Double,
    lvCoefficient: Double,
    skillLevel: Int,
    callback: (Double) -> Double = { result -> ceil(result) }
): D {
    return if (lvCoefficient == 0.0) {
        D.Text(base.toNumStr())
    } else {
        val result = callback(base + lvCoefficient * skillLevel)
        D.Format(
            R.string.formula_base1_lv2_result3,
            arrayOf(
                D.Text(base.toNumStr()),
                D.Text(lvCoefficient.toNumStr()),
                D.Text(result.toNumStr())
            )
        )
    }
}

fun getAtkType(detail: Int): D {
    return D.Format(
        if (detail == 1 || detail == 3) R.string.atk
        else R.string.magic_str
    )
}

fun getAbnormalContent(detail: Int): D {
    return when (detail) {
        3 -> D.Format(R.string.numbness)
        4 -> D.Format(R.string.freeze)
        5 -> D.Format(R.string.fetter)
        6 -> D.Format(R.string.sleep)
        7 -> D.Format(R.string.stand)
        11 -> D.Format(R.string.stop)
        else -> D.Unknown
    }
}

fun getAbnormalDamageContent(detail: Int): D {
    return when (detail) {
        1 -> D.Format(R.string.poison)
        2 -> D.Format(R.string.burn)
        3 -> D.Format(R.string.curse)
        4 -> D.Format(R.string.fierce_poison)
        5 -> D.Format(R.string.beshrew)
        else -> D.Unknown
    }
}

fun getStatusContent(detail: Int): D {
    return when (detail) {
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
        else -> D.Unknown
    }
}

fun getStateContent(detail: Int): D {
    return when (detail) {
        2 -> D.Format(R.string.state_omemechan)
        4 -> D.Format(R.string.state_light_shield)
        50 -> D.Format(R.string.state_servant_intensive)
        57 -> D.Format(R.string.state_flinch)
        60 -> D.Format(R.string.state_otomodachi)
        61 -> D.Format(R.string.state_coin)
        76 -> D.Format(R.string.state_cheru)
        77 -> D.Format(R.string.state_wind_sword)
        90 -> D.Format(R.string.state_tiger_claw)
        92 -> D.Format(R.string.state_holly_night_brilliance)
        97 -> D.Format(R.string.state_ice_dragon_seal)
        106 -> D.Format(R.string.state_strength_seal)
        107 -> D.Format(R.string.state_dragon_eye)
        112 -> D.Format(R.string.state_sword_seal)
        else -> D.Unknown
    }
}

fun getSkillLabel(detail: Int): D {
    return D.Text(
        when (val value = detail / 100 % 10) {
            1 -> "UB"
            else -> "Main ${value - 1}"
        }
    )
}

fun Double.toNumStr(): String {
    val nf = NumberFormat.getInstance()
    nf.isGroupingUsed = false
    nf.maximumFractionDigits = 6
    return nf.format(this)
}
