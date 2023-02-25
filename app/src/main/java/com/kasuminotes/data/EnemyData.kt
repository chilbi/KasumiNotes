package com.kasuminotes.data

data class EnemyData(
    val enemyId: Int,
    val unitId: Int,
    val name: String,
    val unitName: String,
    val searchAreaWidth: Int,
    val atkType: Int,
    val normalAtkCastTime: Float,
    val comment: String,
    val resistStatusId: Int,
    val virtualHp: Int,
    val level: Int,
    val unionBurstLevel: Int,
    val mainSkillLvList: List<Int>,// 1-10
    val exSkillLvList: List<Int>,// 1-5
    val property: Property,
    val multiParts: List<Int>,
    val EnemyMultiParts: List<EnemyData>
) {

}
