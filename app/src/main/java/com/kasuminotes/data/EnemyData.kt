package com.kasuminotes.data

import com.kasuminotes.action.getSkillEffectList
import com.kasuminotes.common.SummonMinion
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getExtraEffectData
import com.kasuminotes.db.getMultiEnemyParts
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
    override var unitSkillData: UnitSkillData? = null,
    var extraEffectData: ExtraEffectData? = null,
    var waveGroupId: Int? = null
): SummonMinion {
    override var skillList: List<SkillItem> = emptyList()
        private set

    suspend fun load(db: AppDatabase, epTableName: String) = withContext(Dispatchers.IO) {
        val list = awaitAll(
            async { db.getUnitAttackPatternList(unitId) },
            async { db.getUnitSkillData(unitId) },
            async { db.getExtraEffectData(if (waveGroupId == null) enemyId else waveGroupId!!, epTableName) }
        )
        @Suppress("UNCHECKED_CAST")
        unitAttackPatternList = list[0] as List<UnitAttackPattern>
        unitSkillData = list[1] as UnitSkillData
        extraEffectData = list[2] as ExtraEffectData?
        skillList = unitSkillData!!.getSkillList(unionBurstLevel, mainSkillLvList, exSkillLvList)

        if (extraEffectData != null) {
            val enemyDataList = db.getMultiEnemyParts(extraEffectData!!.enemyIdList, epTableName)
            val effectSkills = enemyDataList.map { enemyData ->
                async {
                    val skillData = db.getUnitSkillData(enemyData.unitId)
                    val skills = skillData.getSkillList(
                        enemyData.unionBurstLevel,
                        enemyData.mainSkillLvList,
                        enemyData.exSkillLvList
                    )
                    skills
                }
            }.awaitAll().flatten()
            extraEffectData!!.skillEffectList = getSkillEffectList(effectSkills)
        }
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
