package com.kasuminotes.data

import com.kasuminotes.utils.Helper
import kotlin.math.ceil
import kotlin.math.min

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
    var promotionBonusList: List<PromotionBonus> = emptyList()
) {
    fun getEquipMaxLevel(slot: Int): Int = unitPromotion?.getEquip(slot)?.maxEnhanceLevel ?: 0

    fun getProperty(data: UserData): Property {
        return if (
            unitRarity != null &&
            unitPromotionStatus != null &&
            unitPromotion != null &&
            charaStoryStatus != null &&
            exSkillData != null
        ) {
            val rarityLevel = data.charaLevel + data.promotionLevel
            val uniqueLevel = data.uniqueLevel - 1
            val equipsLevel = data.equipsLevel

            val exSkillProperty = exSkillData!!.getProperty(data.rarity, data.exLevel)

            val promotionBonusProperty = promotionBonusList.find {
                it.promotionLevel == data.promotionLevel
            }?.baseProperty ?: Property()

            Property { index ->
                // unitRarity
                var value = unitRarity!!.baseProperty[index]
                value += unitRarity!!.growthProperty[index] * rarityLevel

                // unitPromotionStatus
                value += unitPromotionStatus!!.baseProperty[index]

                // unitPromotion
                unitPromotion!!.equipSlots.forEachIndexed { slotIndex, slot ->
                    value += getEquipValue(index, equipsLevel[slotIndex], slot)
                }

                // uniqueData
                if (uniqueData != null && uniqueLevel > -1) {
                    value += uniqueData!!.baseProperty[index]
                    if (uniqueLevel > 0) {
                        value += ceil(uniqueData!!.growthProperty[index] * uniqueLevel)
                    }
                }

                // selfCharaStoryStatus
                value += getStoryValue(index, data.loveLevel, charaStoryStatus!!.status)

                // sharedCharaStoryStatus
                if (sharedProfiles.isNotEmpty()) {
                    sharedProfiles.forEach { item ->
                        value += getStoryValue(
                            index,
                            item.userData.loveLevel,
                            item.charaStoryStatus!!.status
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
            Property()
        }
    }

    private fun getEquipValue(index: Int, equipLevel: Int, equipData: EquipData?): Double {
        if (equipLevel < 0 || equipData == null) return 0.0

        val level = min(equipLevel, equipData.maxEnhanceLevel)

        if (level == 0) return equipData.baseProperty[index]

        return ceil(equipData.growthProperty[index] * level) + equipData.baseProperty[index]
    }

    private fun getStoryValue(index: Int, loveLevel: Int, status: List<Property>): Double {
        var result = 0.0

        if (loveLevel < 2) return result

        val unlockCount = Helper.getStoryUnlockCount(status.size, loveLevel)

        var i = 0
        while (i < unlockCount) {
            result += status[i++][index]
        }

        return result
    }
}
