package com.kasuminotes.data

data class DungeonAreaData(
    val dungeonAreaId: Int,
    val dungeonName: String,
    val floorNum: Int,
    val mode: Int,
    val pattern: Int,
    val enemyId: Int,
    val unitId: Int,
    val name: String,
    val waveGroupId: Int
)
