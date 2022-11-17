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
    val passiveSkill1: SkillData?,
    val passiveSkill2: SkillData?
) {
    val maxEnhanceLevel: Int = if (rarity > 2) 5 else rarity + 2

    fun getPercentProperty(enhanceLevel: Int): Property {
        if (enhanceLevel < 0) return Property.zero

        return when (val level = min(enhanceLevel, maxEnhanceLevel)) {
            maxEnhanceLevel -> maxProperty
            0 -> defaultProperty
            else -> {
                Property { index ->
                    val maxValue = maxProperty[index]
                    if (maxValue == 0.0) {
                        0.0
                    } else {
                        val defaultValue = defaultProperty[index]
                        if (maxValue < 100.0) {
                            val growthValue = ceil(maxValue / (maxEnhanceLevel + 1))
                            defaultValue + growthValue * level
                        } else {
                            defaultValue + defaultValue * level
                        }
                    }
                }
            }
        }
    }

    fun getProperty(percentProperty: Property, baseProperty: Property): Property {
        return Property { index ->
            val value = percentProperty[index]
            if (value < 100.0) {
                value
            } else {
                baseProperty[index] * value / 10000
            }
        }
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
                        "name," +
                        "description," +
                        "rarity," +
                        "category," +
                        "restriction_id," +
                        "clan_battle_equip_flag," +
                        "passive_skill_id_1," +
                        "passive_skill_id_2," +
                        "passive_skill_power"
            }
            return fields!!
        }
    }
}

