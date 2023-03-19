package com.kasuminotes.data

import com.kasuminotes.common.SummonMinion
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getExSkillData
import com.kasuminotes.db.getUnitAttackPatternList
import com.kasuminotes.db.getUnitPromotion
import com.kasuminotes.db.getUnitPromotionStatus
import com.kasuminotes.db.getUnitRarity
import com.kasuminotes.db.getUnitSkillData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

data class SummonData(
    override val unitId: Int,
    val unitName: String,
    override val searchAreaWidth: Int,
    override val atkType: Int,
    override val normalAtkCastTime: Float,
    var unitRarity: UnitRarity? = null,
    var unitPromotionStatus: UnitPromotionStatus? = null,
    var unitPromotion: UnitPromotion? = null,
    override var unitAttackPatternList: List<UnitAttackPattern> = emptyList(),
    override var unitSkillData: UnitSkillData? = null,
    var exSkillData: ExSkillData? = null
): SummonMinion {
    override val enemyId: Int? = null
    override val name: String get() = unitName

    override var property: Property = Property.zero
        private set

    override var skillList: List<SkillItem> = emptyList()
        private set

    private fun setProperty(level: Int, userData: UserData) {
        if (
            unitRarity != null &&
            unitPromotionStatus != null &&
            unitPromotion != null &&
            exSkillData != null
        ) {
            val rarityLevel = level + userData.promotionLevel
            val equipsLevel = userData.equipsLevel
            val exSkillProperty = exSkillData!!.getProperty(userData.rarity, level)
            property = Property { index ->
                // unitRarity
                var value = unitRarity!!.baseProperty[index]
                value += unitRarity!!.growthProperty[index] * rarityLevel
                // unitPromotionStatus
                value += unitPromotionStatus!!.baseProperty[index]
                // unitPromotion
                unitPromotion!!.equipSlots.forEachIndexed { slotIndex, equipData ->
                    value += equipData?.getPropertyValue(index, equipsLevel[slotIndex]) ?: 0.0
                }
                // exSkillData
                value += exSkillProperty[index]
                value
            }
        }
    }

    private fun setSkillList(level: Int) {
        if (unitSkillData != null) {
            skillList = unitSkillData!!.getSkillList(level)
        }
    }

    suspend fun load(
        db: AppDatabase,
        level: Int,
        userData: UserData,
        defaultDispatcher: CoroutineDispatcher
    ) = withContext(defaultDispatcher) {
        val list = awaitAll(
            async { db.getUnitRarity(unitId, userData.rarity) },
            async { db.getUnitPromotionStatus(unitId, userData.promotionLevel) },
            async { db.getUnitPromotion(unitId, userData.promotionLevel) },
            async { db.getUnitAttackPatternList(unitId) },
            async { db.getUnitSkillData(unitId) },
            async { db.getExSkillData(unitId) }
        )
        unitRarity = list[0] as UnitRarity
        unitPromotionStatus = list[1] as UnitPromotionStatus
        unitPromotion = list[2] as UnitPromotion
        @Suppress("UNCHECKED_CAST")
        unitAttackPatternList = list[3] as List<UnitAttackPattern>
        unitSkillData = list[4] as UnitSkillData
        exSkillData = list[5] as ExSkillData

        setProperty(level, userData)
        setSkillList(level)
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = "unit_name," +
                        "search_area_width," +
                        "atk_type," +
                        "normal_atk_cast_time"
            }
            return fields!!
        }
    }
}
