package com.kasuminotes.data

import com.kasuminotes.action.getStatusIndex
import com.kasuminotes.action.isSelf
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

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
                        if (index < 7) {
                            val growthValue = ceil(maxValue / (maxEnhanceLevel + 1))// TODO 不确定的取整方式
                            defaultValue + growthValue * level
                        } else {
                            defaultValue + defaultValue * level
                        }
                    }
                }
            }
        }
    }

    fun getProperty(percentProperty: Property, baseProperty: Property, includeSkillProperty: Boolean): Property {
        val skillProperty = if (includeSkillProperty) getSkillProperty(baseProperty) else Property.zero
        return Property { index ->
            val value = percentProperty[index]
            if (index < 7) {
                (baseProperty[index] * value / 10000).roundToInt().toDouble() + skillProperty[index]// TODO 不确定的取整方式
            } else {
                value + skillProperty[index]
            }
        }
    }

    private fun getSkillProperty(baseProperty: Property): Property {
        return if (passiveSkill1 == null && passiveSkill2 == null) {
            Property.zero
        } else if (passiveSkill1 != null && passiveSkill2 == null) {
            getPassive90SkillProperty(passiveSkill1, baseProperty)
        } else if (passiveSkill1 == null && passiveSkill2 != null) {
            getPassive90SkillProperty(passiveSkill2, baseProperty)
        } else {
            val p1 = getPassive90SkillProperty(passiveSkill1!!, baseProperty)
            val p2 = getPassive90SkillProperty(passiveSkill2!!, baseProperty)
            Property { i -> p1[i] + p2[i] }
        }
    }

    private fun getPassive90SkillProperty(passiveSkill: SkillData, baseProperty: Property): Property {
        val pairs = mutableListOf<Pair<Int, Double>>()
        val actions = passiveSkill.actions
        actions.forEachIndexed { index, action ->
            if (action.actionType in arrayOf(901, 902) && action.actionDetail1 == 0) {
                var statusActionIndex = index + 1
                if (actions[statusActionIndex].actionType in arrayOf(26, 27, 74)) {
                    statusActionIndex = actions.indexOfFirst { it.actionId == actions[statusActionIndex].actionDetail1 }
                }
                val statusAction = actions[statusActionIndex]
                if (statusAction.isSelf() && statusAction.actionType == 10) {
                    val key = getStatusIndex(statusAction.actionDetail1 / 10)
                    if (key != null) {
                        var value = if (statusAction.actionValue1 == 2.0) {
                            baseProperty[key] * statusAction.actionValue2 / 100
                        } else {
                            statusAction.actionValue2
                        }
                        if (statusAction.actionDetail1 % 10 != 0) {
                            value *= -1
                        }
                        pairs.add(key + 1 to value.roundToInt().toDouble())// TODO 不确定的取整方式
                    }
                }
            }
        }
        return Property(pairs)
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

