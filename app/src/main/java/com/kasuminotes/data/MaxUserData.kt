package com.kasuminotes.data

data class MaxUserData(
    val maxCharaLevel: Int,
    val maxPromotionLevel: Int,
    val maxUniqueLevel: Int,
    val maxArea: Int,
    val maxChara: Int,
    val maxUnique: Int,
    val maxRarity6: Int,
    val userChara: Int,
    val userUnique: Int,
    val userRarity6: Int,
    val connectRankData: ConnectRankData?
) {
    fun getLevelLimit(lvLimitBreak: Int, connectRank: Int): Int {
        val connectRankBonusCharaLevel = connectRankData?.getBonusCharaLevel(connectRank) ?: 0
        return maxCharaLevel + connectRankBonusCharaLevel + lvLimitBreak
    }

    fun isEquippedExUnique1(userData: UserData, unitData: UnitData): Boolean {
        return unitData.hasExUnique1 &&
                userData.unique1Level >= 370 &&
                connectRankData != null &&
                connectRankData.getExUnique1Equipable(userData.connectRank)
    }
}
