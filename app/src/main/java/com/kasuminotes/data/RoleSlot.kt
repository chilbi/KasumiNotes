package com.kasuminotes.data

import com.kasuminotes.R
import com.kasuminotes.action.toNumStr

enum class RoleSlot(
    val parameterType: Int,
    val nameStrId: Int,
    val titleStrId: Int,
    val valueDisplayMode: Int//1直接显示, 2除以100后加%, 3直接加%, 4除以10, 5除以100
) {
    Hp(1, R.string.hp_up, R.string.hp, 2),
    Attack(2, R.string.attack_up, R.string.attack, 2),
    Def(3, R.string.def_up, R.string.def, 2),
    MagicDef(4, R.string.magic_def_up, R.string.magic_def, 2),
    Critical(5, R.string.critical_up, R.string.critical, 2),
    CriticalDamage(6, R.string.critical_damage_up, R.string.critical_damage_up, 2),
    DealDamage(7, R.string.deal_damage_up, R.string.deal_damage_up, 2),
    AbnormalStateAccuracy(8, R.string.abnormal_state_accuracy, R.string.abnormal_state_accuracy, 5),
    AbnormalStateResist(9, R.string.abnormal_state_resist, R.string.abnormal_state_resist, 5),
    NormalAttackEnhance(10, R.string.normal_attack_enhance, R.string.normal_attack_enhance, 5),
    BuffEnhance(11, R.string.buff_enhance, R.string.buff_enhance, 5),
    DebuffEnhance(12, R.string.debuff_enhance, R.string.debuff_enhance, 5),
    HpRecoveryEnhance(13, R.string.hp_recovery_enhance, R.string.hp_recovery_enhance, 5),
    EnergyCharge(14, R.string.energy_charge, R.string.energy_charge, 4);

    fun valueDisplay(enhanceValue: Int): String = when (valueDisplayMode) {
        2 -> "${(enhanceValue.toDouble() / 100.0).toNumStr()}%"
        3 -> "$enhanceValue%"
        4 -> (enhanceValue.toDouble() / 10).toNumStr()
        5 -> (enhanceValue.toDouble() / 100).toNumStr()
        else -> enhanceValue.toString()//1
    }

    companion object {
        private val slotCache = RoleSlot.entries.associateBy { it.parameterType }
        fun fromId(parameterType: Int): RoleSlot? = slotCache[parameterType]
    }
}
