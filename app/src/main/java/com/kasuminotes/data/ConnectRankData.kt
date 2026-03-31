package com.kasuminotes.data

import android.content.Context
import com.kasuminotes.R
import com.kasuminotes.action.toNumStr

data class ConnectRankData(
    val connectRankChartMap: Map<Int/*rank*/, Int/*bonus_level*/>,
    val connectRankBonusList: List<ConnectRankBonus>,
    val connectRankStatusList: List<ConnectRankStatus>
) {
    val maxConnectRank: Int = connectRankChartMap.keys.maxOrNull() ?: 0

    val sumBonusCharaLevel: Int = connectRankBonusList.filter { it.bonusType == 3 }.sumOf { it.value1 }

    fun getBonusCharaLevel(connectRank: Int): Int {
        val bonusLevel = connectRankChartMap.getOrElse(connectRank) { 0 }
        if (bonusLevel < 2 || connectRankBonusList.isEmpty()) return 0
        val bonusList = connectRankBonusList.filter { it.bonusLevel <= bonusLevel && it.bonusType == 3 }
        return bonusList.sumOf { it.value1 }
    }

    fun getExUnique1Equipable(connectRank: Int): Boolean {
        val bonusLevel = connectRankChartMap.getOrElse(connectRank) { 0 }
        if (bonusLevel < 2 || connectRankBonusList.isEmpty()) return false
        return connectRankBonusList.any { it.bonusLevel <= bonusLevel && it.bonusType == 4 }
    }

    fun getStatusList(connectRank: Int, roleId: Int): List<Pair<Int, Int>> {
        val list = mutableListOf<List<Pair<Int, Int>>>()
        connectRankStatusList.forEach {
            val pairs = it.getStatusList(connectRank, roleId)
            if (pairs != null) {
                list.add(pairs)
            }
        }
        return if (list.isEmpty()) {
            connectRankStatusList
                .first { it.roleId == roleId }
                .paramTypeList
                .map { it to 0 }
        } else {
            list.flatten()
                .groupBy({ it.first }, { it.second })
                .map { (key, values) -> key to values.sum() }
                .sortedBy { it.first }
        }
    }

    fun getStatusProperty(base: Property, connectRank: Int, roleId: Int, atkType: Int): Property {
        val statusList = getStatusList(connectRank, roleId)
        val pairs = mutableListOf<Pair<Int, Double>>()
        statusList.forEach { pair ->
            when (pair.first) {
                1 -> pairs.add(1 to base.hp * pair.second / 10000)
                2 -> if (atkType == 1) {
                    pairs.add(2 to base.atk * pair.second / 10000)
                } else {
                    pairs.add(4 to base.magicStr * pair.second / 10000)
                }
                3 -> pairs.add(3 to base.def * pair.second / 10000)
                4 -> pairs.add(5 to base.magicDef * pair.second / 10000)
                //5 -> 造成伤害提升
                //6 -> 受到伤害降低
                //else -> 未知
            }
        }
        return Property(pairs)
    }

    fun getBonusList(connectRank: Int, context: Context): List<ConnectRankBonusItem> {
        val bonusLevel = connectRankChartMap[connectRank] ?: 0
        return connectRankBonusList
            .filter { it.bonusLevel > 1 }
            .map { it.getBonusItem(bonusLevel, context) }
    }
}

data class ConnectRankBonus(
    val bonusLevel: Int,
    val bonusNumber: Int,
    val bonusType: Int,
    val descriptionType: Int,
    val value1: Int,
    val value2: Int,
    val description: String
) {
    fun getBonusItem(bonusLevelValue: Int, context: Context): ConnectRankBonusItem {
        val label = context.getString(when (bonusType) {
            0 -> R.string.lock
            1 -> R.string.talent_bonus_up
            2 -> R.string.party_damage_boost
            3 -> R.string.chara_lv_limit_break
            4 -> R.string.unique1sp_condition
            else -> R.string.unknown_connect_rank_bonus
        })
        val value = when (bonusType) {
            1, 3 -> formatDescription()
            2 -> formatDescription() + " ＋${(value1.toDouble() / 100.0).toNumStr()}%($value2)"
            4 -> context.getString(R.string.unique1sp_enhanceable)
            else -> "values($value1, $value2)"
        }
        val unlock = bonusLevelValue >= bonusLevel
        return ConnectRankBonusItem(label, value, unlock)
    }

    private fun formatDescription(): String {
        val value = when (descriptionType) {
            1 -> value1.toString()
            2 -> (value1.toDouble() / 100.0).toNumStr()
            else -> ""//3
        }
        return description.replace("\\[.*?\\]".toRegex(), "")
            .replace("\\{0\\}".toRegex(), value)

    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = "bonus_level,bonus_number,bonus_type,description_type,value_1,value_2,description"
            }
            return fields!!
        }
    }
}

data class ConnectRankStatus(
    val id: Int,
    val roleId: Int,
    val rankFrom: Int,
    val rankTo: Int,
    val valueList: List<Int>,//1-5
    val paramTypeList: List<Int>//1-5
) {
    fun getStatusList(connectRank: Int, roleIdValue: Int): List<Pair<Int, Int>>? {
        if (roleIdValue != roleId || connectRank < rankFrom) return null
        val maxRank = if (connectRank > rankTo) rankTo else connectRank
        val rank = maxRank - rankFrom + 1
        return valueList.mapIndexed { index, value -> paramTypeList[index] to value * rank }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val valueFields = (1..5).joinToString(",") { i ->
                    "value_$i"
                }
                val paramTypeFields = (1..5).joinToString(",") { i ->
                    "param_type_$i"
                }
                fields = "$valueFields,$paramTypeFields,id,role_id,rank_from,rank_to"
            }
            return fields!!
        }
    }
}

data class ConnectRankBonusItem(
    val label: String,
    val value: String,
    val unlock: Boolean,
)
