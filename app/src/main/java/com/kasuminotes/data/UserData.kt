package com.kasuminotes.data

import com.kasuminotes.ui.theme.rankRarity

data class UserData(
    val userId: Int,
    val unitId: Int,
    val rarity: Int,
    val charaLevel: Int,
    val loveLevel: Int,
    val uniqueLevel: Int,
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
    val equip6Level: Int
) {
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
            uniqueLevel,
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
            equip6Level
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
        fun getFields(pk: Boolean, fk: Boolean): String {
            return (if (pk) "user_id," else "") +
                    (if (fk) "unit_id," else "") +
                    "rarity," +
                    "chara_level," +
                    "love_level," +
                    "unique_level," +
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
                    "equip6_level"
        }
    }
}
