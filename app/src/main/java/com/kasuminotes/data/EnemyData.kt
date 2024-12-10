package com.kasuminotes.data

import com.kasuminotes.common.SummonMinion
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getUnitAttackPatternList
import com.kasuminotes.db.getUnitSkillData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

data class EnemyData(
    override val enemyId: Int,
    override val unitId: Int,
    override val name: String,
    override val searchAreaWidth: Int,
    override val atkType: Int,
    override val normalAtkCastTime: Float,
    val comment: String,
    val uniqueEquipmentFlag1: Int,//0,1,2
    val rarity: Int,
    val promotionLevel: Int,
    val level: Int,
    val unionBurstLevel: Int,
    val mainSkillLvList: List<Int>,// 1-10
    val exSkillLvList: List<Int>,// 1-5
    val multiParts: List<Int>,// 1-5
    override val property: Property,
    var enemyMultiParts: List<EnemyData> = emptyList(),
    override var unitAttackPatternList: List<UnitAttackPattern> = emptyList(),
    override var unitSkillData: UnitSkillData? = null
): SummonMinion {
    override var skillList: List<SkillItem> = emptyList()
        private set

    suspend fun load(db: AppDatabase) = withContext(Dispatchers.IO) {
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
                        "unique_equipment_flag_1," +
                        "rarity," +
                        "promotion_level," +
                        "level," +
                        "union_burst_level"
            }
            return  fields!!
        }
    }
}
