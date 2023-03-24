package com.kasuminotes.data

import com.kasuminotes.common.OrderBy
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getCharaStoryStatus
import com.kasuminotes.db.getExEquipData
import com.kasuminotes.db.getExSkillData
import com.kasuminotes.db.getPromotionBonusList
import com.kasuminotes.db.getPromotions
import com.kasuminotes.db.getUniqueData
import com.kasuminotes.db.getUnitAttackPatternList
import com.kasuminotes.db.getUnitExEquipSlots
import com.kasuminotes.db.getUnitPromotion
import com.kasuminotes.db.getUnitPromotionStatus
import com.kasuminotes.db.getUnitRarity
import com.kasuminotes.db.getUnitSkillData
import com.kasuminotes.utils.Helper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

data class UserProfile(
    var userData: UserData,
    val unitData: UnitData,
    var unitRarity: UnitRarity? = null,
    var unitPromotionStatus: UnitPromotionStatus? = null,
    var unitPromotion: UnitPromotion? = null,
    var uniqueData: UniqueData? = null,
    var charaStoryStatus: CharaStoryStatus? = null,
    var sharedProfiles: List<UserProfile> = emptyList(),
    var promotions: List<UnitPromotion> = emptyList(),
    var unitAttackPatternList: List<UnitAttackPattern> = emptyList(),
    var unitSkillData: UnitSkillData? = null,
    var exSkillData: ExSkillData? = null,
    var promotionBonusList: List<PromotionBonus> = emptyList(),
    var unitConversionData: UnitConversionData? = null,
    var exEquipSlots: List<ExEquipSlot> = emptyList()
) {
    var property: Property? = null
        private set
    var baseProperty: Property? = null
        private set
    var includeExEquipProperty: Property? = null
        private set

    fun getRealUnitData(rarity: Int): UnitData = if (shouldConverted(rarity)) unitConversionData!!.unitData else unitData

    fun getRealUnitAttackPatternList(rarity: Int): List<UnitAttackPattern> = if (shouldConverted(rarity)) unitConversionData!!.unitAttackPatternList else unitAttackPatternList

    fun getRealUnitSkillData(rarity: Int): UnitSkillData? = if (shouldConverted(rarity)) unitConversionData!!.unitSkillData else unitSkillData

    fun getEquipMaxLevel(slot: Int): Int = unitPromotion?.getEquip(slot)?.maxEnhanceLevel ?: 0

    fun getExSkillProperty(data: UserData): Property {
        val realExSkillData = if (shouldConverted(data.rarity)) unitConversionData!!.exSkillData else exSkillData
        return realExSkillData!!.getProperty(data.rarity, data.exLevel)
    }

    fun getExEquipProperty(base: Property, data: UserData): Property {
        return if (exEquipSlots.isEmpty()) {
            Property.zero
        } else {
            val list = exEquipSlots.mapIndexed { index, slot ->
                if (slot.exEquipData == null) {
                    Property.zero
                } else {
                    val percent = slot.exEquipData.getPercentProperty(
                        when (index) {
                            0 -> data.exEquip1Level
                            1 -> data.exEquip2Level
                            else -> data.exEquip3Level
                        }
                    )
                    slot.exEquipData.getProperty(percent, base, true)
                }
            }
            Property { i ->
                var value = 0.0
                list.forEach { item -> value += item[i] }
                value
            }
        }
    }

    fun getProperty(data: UserData): Property {
        return if (
            unitRarity != null &&
            unitPromotionStatus != null &&
            unitPromotion != null &&
            charaStoryStatus != null &&
            exSkillData != null
        ) {
            val rarityLevel = data.charaLevel + data.promotionLevel
            val equipsLevel = data.equipsLevel

            val uniqueProperty = uniqueData?.getProperty(data.uniqueLevel)
            val exSkillProperty = getExSkillProperty(data)

            val promotionBonusProperty = promotionBonusList.find {
                it.promotionLevel == data.promotionLevel
            }?.bonusProperty ?: Property.zero

            Property { index ->
                // unitRarity
                var value = unitRarity!!.baseProperty[index]
                value += unitRarity!!.growthProperty[index] * rarityLevel

                // unitPromotionStatus
                value += unitPromotionStatus!!.baseProperty[index]

                // unitPromotion
                unitPromotion!!.equipSlots.forEachIndexed { slotIndex, equipData ->
                    value += equipData?.getPropertyValue(index, equipsLevel[slotIndex]) ?: 0.0
                }

                // uniqueData
                if (uniqueProperty != null) {
                    value += uniqueProperty[index]
                }

                // selfCharaStoryStatus
                value += getStoryValue(index, data.loveLevel, charaStoryStatus!!.status, unitData.maxRarity)

                // sharedCharaStoryStatus
                if (sharedProfiles.isNotEmpty()) {
                    sharedProfiles.forEach { item ->
                        value += getStoryValue(
                            index,
                            item.userData.loveLevel,
                            item.charaStoryStatus!!.status,
                            item.unitData.maxRarity
                        )
                    }
                }

                // exSkillData
                value += exSkillProperty[index]

                // promotionBonus
                value += promotionBonusProperty[index]

                value
            }
        } else {
            Property.zero
        }
    }

    fun getIntOf(orderBy: OrderBy): Int = when (orderBy) {
        OrderBy.StartTime -> unitData.startTimeInt
        OrderBy.CharaId -> unitData.unitId
        OrderBy.Rarity -> userData.rarity
        OrderBy.SearchAreaWidth -> unitData.searchAreaWidth
        OrderBy.Age -> unitData.age.toIntOrNull() ?: 9999
        OrderBy.Height -> unitData.height.toIntOrNull() ?: 9999
        OrderBy.Weight -> unitData.weight.toIntOrNull() ?: 9999
        /*OrderBy.Hp -> property?.hp?.roundToInt() ?: 0
        OrderBy.Atk -> property?.atk?.roundToInt() ?: 0
        OrderBy.MagicStr -> property?.magicStr?.roundToInt() ?: 0
        OrderBy.Def -> property?.def?.roundToInt() ?: 0
        OrderBy.MagicDef -> property?.magicDef?.roundToInt() ?: 0
        OrderBy.PhysicalCritical -> property?.physicalCritical?.roundToInt() ?: 0
        OrderBy.MagicCritical -> property?.magicCritical?.roundToInt() ?: 0
        OrderBy.EnergyRecoveryRate -> property?.energyRecoveryRate?.roundToInt() ?: 0
        OrderBy.EnergyReduceRate -> property?.energyReduceRate?.roundToInt() ?: 0*/
    }

    fun getStringOf(orderBy: OrderBy): String = when (orderBy) {
        OrderBy.StartTime -> unitData.startTimeStr
        OrderBy.CharaId -> (unitData.unitId / 100).toString()
        OrderBy.Rarity -> ""
        OrderBy.SearchAreaWidth -> unitData.searchAreaWidth.toString()
        OrderBy.Age -> unitData.age
        OrderBy.Height -> unitData.height
        OrderBy.Weight -> unitData.weight
        /*OrderBy.Hp -> (property?.hp?.roundToInt() ?: 0).toString()
        OrderBy.Atk -> (property?.atk?.roundToInt() ?: 0).toString()
        OrderBy.MagicStr -> (property?.magicStr?.roundToInt() ?: 0).toString()
        OrderBy.Def -> (property?.def?.roundToInt() ?: 0).toString()
        OrderBy.MagicDef -> (property?.magicDef?.roundToInt() ?: 0).toString()
        OrderBy.PhysicalCritical -> (property?.physicalCritical?.roundToInt() ?: 0).toString()
        OrderBy.MagicCritical -> (property?.magicCritical?.roundToInt() ?: 0).toString()
        OrderBy.EnergyRecoveryRate -> (property?.energyRecoveryRate?.roundToInt() ?: 0).toString()
        OrderBy.EnergyReduceRate -> (property?.energyReduceRate?.roundToInt() ?: 0).toString()*/
    }

    fun setProperty(p: Property, base: Property, includeExEquip: Property) {
        property = p
        baseProperty = base
        includeExEquipProperty = includeExEquip
    }

    private fun shouldConverted(rarity: Int): Boolean = unitConversionData != null && rarity > 5

    private fun getStoryValue(index: Int, loveLevel: Int, status: List<Property>, maxRarity: Int): Double {
        var result = 0.0

        if (loveLevel < 2) return result

        val unlockCount = Helper.getStoryUnlockCount(
            status.size,
            loveLevel,
            maxRarity,
            Helper.getStoryDiffCount(status.size, maxRarity)
        )

        var i = 0
        while (i < unlockCount) {
            result += status[i++][index]
        }

        return result
    }

    suspend fun load(
        db: AppDatabase,
        profiles: List<UserProfile>,
        defaultDispatcher: CoroutineDispatcher
    ) = withContext(defaultDispatcher) {
        val list = awaitAll(
            async { db.getUnitRarity(unitData.unitId, userData.rarity) },
            async { db.getUnitPromotionStatus(unitData.unitId, userData.promotionLevel) },
            async { db.getUnitPromotion(unitData.unitId, userData.promotionLevel) },
            async { db.getUniqueData(unitData.equipId) },
            async { charaStoryStatus ?: db.getCharaStoryStatus(unitData.unitId) },
            async { db.getPromotions(unitData.unitId) },
            async { db.getUnitAttackPatternList(unitData.unitId) },
            async { db.getUnitSkillData(unitData.unitId) },
            async { db.getExSkillData(unitData.unitId) },
            async { db.getPromotionBonusList(unitData.unitId) },
            async { unitConversionData?.let { db.getUnitAttackPatternList(it.convertedUnitId) } },
            async { unitConversionData?.let { db.getUnitSkillData(it.convertedUnitId) } },
            async { unitConversionData?.let { db.getExSkillData(it.convertedUnitId) } },
            async { db.getUnitExEquipSlots(unitData.unitId) },
            async { if (userData.exEquip1 == 0) null else db.getExEquipData(userData.exEquip1) },
            async { if (userData.exEquip2 == 0) null else db.getExEquipData(userData.exEquip2) },
            async { if (userData.exEquip3 == 0) null else db.getExEquipData(userData.exEquip3) }
        )
        unitRarity = list[0] as UnitRarity
        unitPromotionStatus = list[1] as UnitPromotionStatus
        unitPromotion = list[2] as UnitPromotion
        uniqueData = list[3] as UniqueData?
        charaStoryStatus = list[4] as CharaStoryStatus
        @Suppress("UNCHECKED_CAST")
        promotions = list[5] as List<UnitPromotion>
        @Suppress("UNCHECKED_CAST")
        unitAttackPatternList = list[6] as List<UnitAttackPattern>
        unitSkillData = list[7] as UnitSkillData
        exSkillData = list[8] as ExSkillData
        @Suppress("UNCHECKED_CAST")
        promotionBonusList = list[9] as List<PromotionBonus>
        if (unitConversionData != null) {
            @Suppress("UNCHECKED_CAST")
            unitConversionData!!.unitAttackPatternList = list[10] as List<UnitAttackPattern>
            unitConversionData!!.unitSkillData = list[11] as UnitSkillData
            unitConversionData!!.exSkillData = list[12] as ExSkillData
        }
        var listIndex = 14
        @Suppress("UNCHECKED_CAST")
        exEquipSlots = (list[13] as List<ExEquipSlot>).map {
            it.copy(exEquipData = list[listIndex++] as ExEquipData?)
        }
        val sharedChara = charaStoryStatus!!.sharedChara
        if (sharedChara.isNotEmpty()) {
            val sharedProfileList = profiles
                .filter { sharedChara.contains(it.unitData.unitId) }
                .sortedByDescending { it.unitData.unitId }
            sharedProfileList.map { item ->
                async {
                    if (item.charaStoryStatus == null) {
                        item.charaStoryStatus = db.getCharaStoryStatus(item.unitData.unitId)
                    }
                }
            }.awaitAll()
            sharedProfiles = sharedProfileList
        }
    }
}
