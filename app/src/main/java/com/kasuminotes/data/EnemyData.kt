package com.kasuminotes.data

import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getUnitAttackPatternList
import com.kasuminotes.db.getUnitSkillData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

data class EnemyData(
    val enemyId: Int,
    val unitId: Int,
    val name: String,
    val searchAreaWidth: Int,
    val atkType: Int,
    val normalAtkCastTime: Float,
    val comment: String,
    val level: Int,
    val unionBurstLevel: Int,
    val mainSkillLvList: List<Int>,// 1-10
    val exSkillLvList: List<Int>,// 1-5
    val multiParts: List<Int>,// 1-5
    val property: Property,
    var EnemyMultiParts: List<EnemyData> = emptyList(),
    var unitAttackPatternList: List<UnitAttackPattern> = emptyList(),
    var unitSkillData: UnitSkillData? = null
) {
    var skillList: List<SkillItem> = emptyList()
        private set

    suspend fun load(
        db: AppDatabase,
        defaultDispatcher: CoroutineDispatcher
    ) = withContext(defaultDispatcher) {
        val list = awaitAll(
            async { db.getUnitAttackPatternList(unitId) },
            async { db.getUnitSkillData(unitId) }
        )
        @Suppress("UNCHECKED_CAST")
        unitAttackPatternList = list[0] as List<UnitAttackPattern>
        unitSkillData = list[1] as UnitSkillData
        skillList = unitSkillData!!.getSkillList(unionBurstLevel, mainSkillLvList, exSkillLvList)
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val mainSkillLvFields = (1..10).joinToString(",") { i ->
                    "main_skill_lv_$i"
                }
                val exSkillLvFields = (1..5).joinToString(",") { i->
                    "ex_skill_lv_$i"
                }
                val childEnemyParameterFields = (1..5).joinToString(",") { i ->
                    "child_enemy_parameter_$i"
                }

                fields = "$mainSkillLvFields,$exSkillLvFields,$childEnemyParameterFields,${Property.getFields()}," +
                        "ep.enemy_id," +
                        "ep.unit_id," +
                        "ep.name," +
                        "search_area_width," +
                        "atk_type," +
                        "normal_atk_cast_time," +
                        "comment," +
                        "level," +
                        "union_burst_level"
            }
            return  fields!!
        }
    }
}
