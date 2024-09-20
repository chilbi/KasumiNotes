package com.kasuminotes.action

import androidx.annotation.StringRes
import com.kasuminotes.R
import com.kasuminotes.common.Label
import com.kasuminotes.data.Property
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.floor

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

fun getAbnormalContent(detail: Int): D {
    return when (detail) {
        1 -> D.Format(R.string.slow)
        2 -> D.Format(R.string.haste)
        3 -> D.Format(R.string.numbness)
        4 -> D.Format(R.string.freeze)
        5 -> D.Format(R.string.fetter)
        6 -> D.Format(R.string.sleep)
        7 -> D.Format(R.string.stand)
        8 -> D.Format(R.string.petrifaction)
        9 -> D.Format(R.string.detention)
        10 -> D.Format(R.string.coma)
        11 -> D.Format(R.string.stop)
        13 -> D.Format(R.string.crystallization)
        else -> D.Unknown
    }
}

fun getAbnormalDamageContent(detail: Int): D {
    return when (detail) {
        0 -> D.Format(R.string.detention_damage)
        1 -> D.Format(R.string.poison)
        2 -> D.Format(R.string.burn)
        3 -> D.Format(R.string.curse)
        4 -> D.Format(R.string.fierce_poison)
        5 -> D.Format(R.string.beshrew)
        9 -> D.Format(R.string.black_fire)
        else -> D.Unknown
    }
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
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 4
        5 -> 7
        6 -> 5
        7 -> 6
        8 -> 13
        9 -> 8
        else -> null
    }
}

fun getStateContent(detail: Int, actionId: Int): D {
    return when (detail) {
        1 -> D.Format(R.string.state_hiding)
        2 -> D.Format(R.string.state_omemechan)
        4 -> D.Format(R.string.state_light_shield)
        5 -> D.Format(R.string.state_utsusemi)
        6 -> D.Format(R.string.state_ocean_type)
        50 -> D.Format(R.string.state_servant_intensive)
        57 -> D.Format(R.string.state_flinch)
        60 -> D.Format(R.string.state_otomodachi)
        61 -> D.Format(R.string.state_coin)
        63 -> D.Format(R.string.state_black_star)
        72 -> D.Format(R.string.state_knight_guard)
        76 -> D.Format(R.string.state_cheru)
        77 -> D.Format(R.string.state_wind_sword)
        90 -> D.Format(R.string.state_tiger_claw)
        92 -> D.Format(R.string.state_holly_night_brilliance)
        97 -> D.Format(R.string.state_ice_dragon_seal)
        98 -> D.Format(R.string.state_study_time)
        106 -> D.Format(R.string.state_strength_seal)
        107 -> D.Format(R.string.state_dragon_eye)
        111 -> D.Format(R.string.state_overload_structure)
        112 -> D.Format(R.string.state_sword_seal)
        113 -> D.Format(R.string.state_soul_anchor_seal)
        115 -> D.Format(R.string.state_happy_times)
        116 -> D.Format(R.string.state_water_blade_intensive)
        117 -> D.Format(R.string.state_butterfly_seal)
        118 -> D.Format(R.string.state_fuwafuwa_wool)
        119 -> D.Format(R.string.state_dark_night_intensive)
        120 -> D.Format(R.string.state_soul_ability)
        122 -> D.Format(R.string.state_yasakaninomagatama)
        125 -> if (actionId / 100000 == 1061) D.Format(R.string.state_sword)
        else D.Format(R.string.state_black_star)
        126 -> D.Format(R.string.state_friendship_seal)
        127 -> D.Format(R.string.state_hazy)
        130 -> D.Format(R.string.state_kizuna_certificate)
        131 -> D.Format(R.string.state_psi_charge)
        133 -> D.Format(R.string.state_universe_seal)
        134 -> D.Format(R.string.state_beauty)
        135 -> D.Format(R.string.state_battery)
        137 -> D.Format(R.string.state_kairai)
        139 -> D.Format(R.string.state_roar_of_wolf_tooth)
        140 -> D.Format(R.string.state_fire_dance_seal)
        142 -> D.Format(R.string.state_returning_rose)
        145 -> D.Format(R.string.state_puukichi_cushion)
        156 -> D.Format(R.string.state_peace_by_the_waterfront)
        152 -> D.Format(R.string.state_flower_trick)
        157 -> D.Format(R.string.state_moon_water)
        158 -> D.Format(R.string.state_poison_flick)
        159 -> D.Format(R.string.state_kababankun)
        160 -> D.Format(R.string.state_nebaneba)
        161 -> D.Format(R.string.state_iron_guard)
        162 -> D.Format(R.string.state_boumajin)
        171 -> D.Format(R.string.state_storm)
        173 -> D.Format(R.string.state_solar_eclipse)
        175 -> D.Format(R.string.state_ice)
        176 -> D.Format(R.string.state_illusion)
        177 -> D.Format(R.string.state_kika)
        178 -> D.Format(R.string.state_mania)
        183 -> D.Format(R.string.state_curtain_call)
        188 -> D.Format(R.string.state_passionate_bouquet)
        189 -> D.Format(R.string.state_teapot)
        else -> D.Format(R.string.state_unknown, arrayOf(D.Text(detail.toString())))
    }
}

fun getUnitName(id: Int): D {
    return when (id) {
        418101 -> D.Format(R.string.summon_ore_dragon)
        425801 -> D.Format(R.string.summon_shadow_425801)
        425802 -> D.Format(R.string.summon_shadow_425802)
        426201 -> D.Format(R.string.summon_family_426201)
        426202 -> D.Format(R.string.summon_family_426202)
        427101 -> D.Format(R.string.summon_skull_427101)
        else -> D.Format(R.string.summon_unknown_id1, arrayOf(D.Text(id.toString())))
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
    nf.maximumFractionDigits = 6
    return nf.format(this)
}
