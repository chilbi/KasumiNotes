package com.kasuminotes.data

import kotlin.math.ceil
import kotlin.math.min

data class ExEquipData(
    val exEquipmentId: Int,
    val name: String,
    val description: String,
    val rarity: Int,
    val category: Int,
    val restrictionId: Int,
    val clanBattleEquipFlag: Int,
    val passiveSkillId1: Int,
    val passiveSkillId2: Int,
    val passiveSkillPower: Int,
    val defaultProperty: Property,
    val maxProperty: Property,
    val passiveSkill1: SkillData? = null,
    val passiveSkill2: SkillData? = null
) {
    val maxEnhanceLevel: Int = if (rarity > 2) 5 else rarity + 2

    fun getProperty(enhanceLevel: Int): Property {
        if (enhanceLevel < 0) return Property()

        val level = min(enhanceLevel, maxEnhanceLevel)

        if (level == 0) return defaultProperty

        return maxProperty
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val defaultFields = Property.keys.joinToString(",") { key ->
                    "default_$key"
                }
                val maxFields = Property.keys.joinToString(",") { key ->
                    "max_$key"
                }
                fields = "$defaultFields,$maxFields," +
                        "ex_equipment_id," +
                        "name," +
                        "description," +
                        "rarity," +
                        "category," +
                        "restriction_id" +
                        "clan_battle_equip_flag," +
                        "passive_skill_id_1," +
                        "passive_skill_id_2" +
                        "passive_skill_power"
            }
            return fields!!
        }
    }
}

//    val defaultHp: Int,
//    val maxHp: Int,
//    val defaultAtk: Int,
//    val maxAtk: Int,
//    val defaultMagicStr: Int,
//    val maxMagicStr: Int,
//    val defaultDef: Int,
//    val maxDef: Int,
//    val defaultMagicDef: Int,
//    val maxMagicDef: Int,
//    val defaultPhysicalCritical: Int,
//    val maxPhysicalCritical: Int,
//    val defaultMagicCritical: Int,
//    val maxMagicCritical: Int,
//    val defaultWaveHpRecovery: Int,
//    val maxWaveHpRecovery: Int,
//    val defaultWaveEnergyRecovery: Int,
//    val maxWaveEnergyRecovery: Int,
//    val defaultDodge: Int,
//    val maxDodge: Int,
//    val defaultPhysicalPenetrate: Int,
//    val maxPhysicalPenetrate: Int,
//    val defaultMagicPenetrate: Int,
//    val maxMagicPenetrate: Int,
//    val defaultLifeSteal: Int,
//    val maxLifeSteal: Int,
//    val defaultHpRecoveryRate: Int,
//    val maxHpRecoveryRate: Int,
//    val defaultEnergyRecoveryRate: Int,
//    val maxEnergyRecoveryRate: Int,
//    val defaultEnergyReduceRate: Int,
//    val maxEnergyReduceRate: Int,
//    val defaultAccuracy: Int,
//    val maxAccuracy: Int
