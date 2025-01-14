package com.kasuminotes.data

import com.kasuminotes.ui.theme.rankRarity
import org.json.JSONObject

data class UserData(
    val userId: Int,
    val unitId: Int,
    val rarity: Int,
    val charaLevel: Int,
    val loveLevel: Int,
    val unique1Level: Int,
    val unique2Level: Int,
    val promotionLevel: Int,
    val ubLevel: Int,
    val skill1Level: Int,
    val skill2Level: Int,
    val exLevel: Int,
    val equip1Level: Int,
    val equip2Level: Int,
    val equip3Level: Int,
    val equip4Level: Int,
    val equip5Level: Int,
    val equip6Level: Int,
    val exEquip1: Int = 0,
    val exEquip2: Int = 0,
    val exEquip3: Int = 0,
    val exEquip1Level: Int = -1,
    val exEquip2Level: Int = -1,
    val exEquip3Level: Int = -1,
    val subPercentJson: String = "",
    val lvLimitBreak: Int = 0
) {
    val subPercentMap: Map<Int, List<Pair<Int, Double>>> = toSubPercentMap(subPercentJson)

    val rankRarity: Int
        get() = promotionLevel.rankRarity

    val equipsLevel: List<Int>
        get() = listOf(equip1Level, equip2Level, equip3Level, equip4Level, equip5Level, equip6Level)

    val stringValues: String
        get() = listOf(
            userId,
            unitId,
            rarity,
            charaLevel,
            loveLevel,
            unique1Level,
            unique2Level,
            promotionLevel,
            ubLevel,
            skill1Level,
            skill2Level,
            exLevel,
            equip1Level,
            equip2Level,
            equip3Level,
            equip4Level,
            equip5Level,
            equip6Level,
            exEquip1,
            exEquip2,
            exEquip3,
            exEquip1Level,
            exEquip2Level,
            exEquip3Level,
            "'$subPercentJson'"
        ).joinToString(",")

    fun getEquipLevel(slot: Int): Int = when (slot) {
        1 -> equip1Level
        2 -> equip2Level
        3 -> equip3Level
        4 -> equip4Level
        5 -> equip5Level
        else -> equip6Level
    }

    companion object {
        private var fields: String? = null

        fun getFields(pk: Boolean, fk: Boolean): String {
            if (fields == null) {
                fields = "rarity," +
                        "chara_level," +
                        "love_level," +
                        "unique1_level," +
                        "unique2_level," +
                        "promotion_level," +
                        "ub_level," +
                        "skill1_level," +
                        "skill2_level," +
                        "ex_level," +
                        "equip1_level," +
                        "equip2_level," +
                        "equip3_level," +
                        "equip4_level," +
                        "equip5_level," +
                        "equip6_level," +
                        "ex_equip1," +
                        "ex_equip2," +
                        "ex_equip3," +
                        "ex_equip1_level," +
                        "ex_equip2_level," +
                        "ex_equip3_level," +
                        "sub_percent_json"
            }
            return (if (pk) "user_id," else "") + (if (fk) "unit_id," else "") + fields
        }

        fun toSubPercentJson(subPercentMap: Map<Int, List<Pair<Int, Double>>>): String {
            return if (subPercentMap.isEmpty()) {
                ""
            } else {
                val json = StringBuilder()
                json.append("{")
                var hasPrevSlot = false
                for ((slotNum, subPercentList) in subPercentMap) {
                    if (hasPrevSlot) {
                        json.append(",")
                    }
                    json.append("\"$slotNum\":[")
                    var hasPrevItem = false
                    for (subPercent in subPercentList) {
                        if (hasPrevItem) {
                            json.append(",")
                        }
                        json.append("{")
                        json.append("\"${subPercent.first}\":${subPercent.second}")
                        json.append("}")
                        hasPrevItem = true
                    }
                    json.append("]")
                    hasPrevSlot = true
                }
                json.append("}")
                json.toString()
            }
        }

        fun toSubPercentMap(subPercentJson: String): Map<Int, List<Pair<Int, Double>>> {
            return if (subPercentJson == "") {
                emptyMap()
            } else {
                val map = mutableMapOf<Int, List<Pair<Int, Double>>>()
                val jsonObject = JSONObject(subPercentJson)
                val slotKeys = jsonObject.keys()
                while (slotKeys.hasNext()) {
                    val list = mutableListOf<Pair<Int, Double>>()
                    val slotKey = slotKeys.next()
                    val jsonArray = jsonObject.getJSONArray(slotKey)
                    for (i in 0 until jsonArray.length()) {
                        val itemObject = jsonArray.getJSONObject(i)
                        val itemKeys = itemObject.keys()
                        while (itemKeys.hasNext()) {
                            val key = itemKeys.next()
                            val value = itemObject.getDouble(key)
                            list.add(key.toInt() to value)
                        }
                    }
                    map[slotKey.toInt()] = list
                }
                map
            }
        }
    }
}
