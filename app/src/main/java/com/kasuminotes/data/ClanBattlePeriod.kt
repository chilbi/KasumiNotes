package com.kasuminotes.data

import com.kasuminotes.R

data class ClanBattlePeriod(
    val clanBattleId: Int,
    val startTime: String,
    val bossUnitIdList: List<Int>,// 1-5
    var mapDataList: List<ClanBattleMapData> = emptyList()
) {
    val period: Int = clanBattleId - 1000

    val constellation: Int by lazy {
        val n = period % 12
        val index = if (period <= 12) {
            period
        } else if (n == 0) {
            12
        } else {
            n
        }
        constellationStrResList[index - 1]
    }

    val year: Int by lazy { dateRegex.find(startTime)?.groupValues?.get(1)?.toInt() ?: 0 }

    val month: Int by lazy { dateRegex.find(startTime)?.groupValues?.get(2)?.toInt() ?: 0 }

    companion object {
        val dateRegex = Regex("^(\\d+)/(\\d+)/")

        val constellationStrResList: List<Int> = listOf(
            R.string.aries,
            R.string.taurus,
            R.string.gemini,
            R.string.cancer,
            R.string.leo,
            R.string.virgo,
            R.string.libra,
            R.string.scorpio,
            R.string.sagittarius,
            R.string.capricorn,
            R.string.aquarius,
            R.string.pisces
        )
    }
}
