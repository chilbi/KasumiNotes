package com.kasuminotes.data

data class ClanBattleMapData(
    val phase: Int,
    val lapNumFrom: Int,
    val lapNumTo: Int,
    val waveGroupIdList: List<Int>,// 1-5
    val scoreCoefficientList: List<Float>,
) {

}
