package com.kasuminotes.data

import com.kasuminotes.R
import com.kasuminotes.action.toNumStr

enum class SkillNode(
    val parameterType: Int,
    val titleStrId: Int,
    val valueDisplayMode: Int////1直接显示, 2除以100后加%, 3直接加%, 4除以10, 5除以100
) {
    TalentAtk(-1, R.string.talent_atk, 1),
    Hp(1, R.string.hp, 2),
    Atk(2, R.string.atk, 2),
    Def(3, R.string.def, 2),
    MagicStr(4, R.string.magic_str, 2),
    MagicDef(5, R.string.magic_def, 2),
    PhysicalCritical(6, R.string.physical_critical, 2),
    MagicCritical(7, R.string.magic_critical, 2),
    Dodge(8, R.string.dodge, 1),
    LifeSteal(9, R.string.life_steal, 1),
    WaveHpRecovery(10, R.string.wave_hp_recovery, 1),
    WaveEnergyRecovery(11, R.string.wave_energy_recovery, 1),
    PhysicalPenetrate(12, R.string.physical_penetrate, 1),
    MagicPenetrate(13, R.string.magic_penetrate, 1),
    EnergyRecoveryRate(14, R.string.energy_recovery_rate, 1),
    HpRecoveryRate(15, R.string.hp_recovery_rate, 1),
    EnergyReduceRate(16, R.string.energy_reduce_rate, 1),
    Accuracy(17, R.string.accuracy, 1),
    AtkAndMagicStr(50, R.string.skill_node_atk_and_magic_str, 2),
    DefAndMagicDef(51, R.string.skill_node_def_and_magic_def, 2),
    PhysicalAndMagicCritical(52, R.string.skill_node_physical_and_magic_critical, 2),
    PhysicalCriticalDamage(100, R.string.skill_node_physical_critical_damage, 3),
    MagicCriticalDamage(101, R.string.skill_node_magic_critical_damage, 3),
    PhysicalDamage(102, R.string.skill_node_physical_damage, 3),
    MagicDamage(103, R.string.skill_node_magic_damage, 3),
    TalentBonus(104, R.string.skill_node_talent_bonus, 2);

    fun valueDisplay(enhanceValue: Int): String = when (valueDisplayMode) {
        2 -> "${(enhanceValue.toDouble() / 100.0).toNumStr()}%"
        3 -> "$enhanceValue%"
        4 -> (enhanceValue.toDouble() / 10).toNumStr()
        5 -> (enhanceValue.toDouble() / 100).toNumStr()
        else -> enhanceValue.toString()//1
    }

    companion object {
        private val nodeCache = SkillNode.entries.associateBy { it.parameterType }
        fun fromId(parameterType: Int): SkillNode? = nodeCache[parameterType]
    }
}
