package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.CharaStoryStatus
import com.kasuminotes.data.ExSkillData
import com.kasuminotes.data.PromotionBonus
import com.kasuminotes.data.Property
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UnitPromotionStatus
import com.kasuminotes.data.UnitRarity
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.db.AppDatabase
import com.kasuminotes.db.getCharaStoryStatus
import com.kasuminotes.db.getExSkillData
import com.kasuminotes.db.getPromotionBonusList
import com.kasuminotes.db.getPromotions
import com.kasuminotes.db.getUniqueData
import com.kasuminotes.db.getUnitAttackPatternList
import com.kasuminotes.db.getUnitPromotion
import com.kasuminotes.db.getUnitPromotionStatus
import com.kasuminotes.db.getUnitRarity
import com.kasuminotes.db.getUnitSkillData
import com.kasuminotes.db.putUserData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharaState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private var onMaxUserDataChange: (userUniqueDiff: Int, userRarity6Diff: Int) -> Unit,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private var backupUnitRarity: UnitRarity? = null
    private var backupUnitPromotionStatus: UnitPromotionStatus? = null
    private var backupUnitPromotion: UnitPromotion? = null

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var userData by mutableStateOf<UserData?>(null)
        private set
    var property by mutableStateOf(Property())
        private set
    var originProperty by mutableStateOf(property)
        private set
    var saveVisible by mutableStateOf(false)
        private set

    fun selectUserProfile(data: UserProfile, profiles: List<UserProfile>, maxCharaLevel: Int) {
        restore()

        if (data.userData.charaLevel > maxCharaLevel) {
            data.userData = data.userData.copy(lvLimitBreak = 10)
        }
        userProfile = data
        userData = data.userData
        saveVisible = false

        if (
            data.unitRarity == null ||
            data.unitPromotionStatus == null ||
            data.unitPromotion == null ||
            data.charaStoryStatus == null ||
            data.unitSkillData == null ||
            data.exSkillData == null
        ) {
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()

                data.loadAll(db)

                val sharedChara = data.charaStoryStatus!!.sharedChara

                if (sharedChara.isNotEmpty()) {
                    val sharedProfiles = profiles
                        .filter { sharedChara.contains(it.unitData.unitId) }
                        .sortedByDescending { it.unitData.unitId }

                    sharedProfiles.map { item ->
                        async { item.loadStory(db) }
                    }.awaitAll()

                    data.sharedProfiles = sharedProfiles
                }

                backupUnitRarity = data.unitRarity
                backupUnitPromotionStatus = data.unitPromotionStatus
                backupUnitPromotion = data.unitPromotion

                property = data.getProperty(data.userData)
                originProperty = property
            }
        } else {
            backupUnitRarity = data.unitRarity
            backupUnitPromotionStatus = data.unitPromotionStatus
            backupUnitPromotion = data.unitPromotion

            property = data.getProperty(data.userData)
            originProperty = property
        }
    }

    fun changeLvLimitBreak(maxCharaLevel: Int) {
        val lvLimitBreak = if (userData!!.lvLimitBreak > 0) 0 else 10
        val value = maxCharaLevel + lvLimitBreak
        userData = userData!!.copy(
            charaLevel = value,
            ubLevel = value,
            skill1Level = value,
            skill2Level = value,
            exLevel = value,
            lvLimitBreak = lvLimitBreak
        )
        changeState()
    }

    fun changeCharaLevel(value: Int) {
        userData = userData!!.copy(
            charaLevel = value,
            ubLevel = value,
            skill1Level = value,
            skill2Level = value,
            exLevel = value
        )
        changeState()
    }

    fun changeRarity(value: Int) {
        if (value > 0 && value != userData!!.rarity) {
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()
                val unitRarity = db.getUnitRarity(userProfile!!.unitData.unitId, value)
                userProfile!!.unitRarity = unitRarity
                userData = userData!!.copy(rarity = value)
                changeState()
            }
        }
    }

    fun changeUniqueLevel(value: Int) {
        userData = userData!!.copy(uniqueLevel = value)
        changeState()
    }

    fun changeLoveLevel(value: Int) {
        userData = userData!!.copy(loveLevel = value)
        changeState()
    }

    fun changePromotionLevel(value: Int) {
        if (value != userData!!.promotionLevel) {
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()
                val unitPromotionStatus = db.getUnitPromotionStatus(userProfile!!.unitData.unitId, value)
                val promotions = userProfile!!.promotions
                val unitPromotion = promotions[promotions.size - value]
                userProfile!!.unitPromotionStatus = unitPromotionStatus
                userProfile!!.unitPromotion = unitPromotion
                userData = userData!!.copy(
                    promotionLevel = value,
                    equip1Level = unitPromotion.equipSlot1?.maxEnhanceLevel ?: -1,
                    equip2Level = unitPromotion.equipSlot2?.maxEnhanceLevel ?: -1,
                    equip3Level = unitPromotion.equipSlot3?.maxEnhanceLevel ?: -1,
                    equip4Level = unitPromotion.equipSlot4?.maxEnhanceLevel ?: -1,
                    equip5Level = unitPromotion.equipSlot5?.maxEnhanceLevel ?: -1,
                    equip6Level = unitPromotion.equipSlot6?.maxEnhanceLevel ?: -1
                )
                changeState()
            }
        }
    }

    fun changeSkillLevel(value: Int, labelText: String) {
        userData = when (labelText) {
            "UB" -> userData!!.copy(ubLevel = value)
            "EX" -> userData!!.copy(exLevel = value)
            "Main 1" -> userData!!.copy(skill1Level = value)
            else -> userData!!.copy(skill2Level = value)
        }
        changeState()
    }

    fun changeEquipLevel(slot: Int, value: Int) {
        userData = when (slot) {
            1 -> userData!!.copy(equip1Level = value)
            2 -> userData!!.copy(equip2Level = value)
            3 -> userData!!.copy(equip3Level = value)
            4 -> userData!!.copy(equip4Level = value)
            5 -> userData!!.copy(equip5Level = value)
            else -> userData!!.copy(equip6Level = value)
        }
        changeState()
    }

    fun cancel() {
        userProfile!!.unitRarity = backupUnitRarity
        userProfile!!.unitPromotionStatus = backupUnitPromotionStatus
        userProfile!!.unitPromotion = backupUnitPromotion

        userData = userProfile!!.userData
        changeState()
    }

    fun save() {
        backupUnitRarity = userProfile!!.unitRarity
        backupUnitPromotionStatus = userProfile!!.unitPromotionStatus
        backupUnitPromotion = userProfile!!.unitPromotion

        var userUniqueDiff = 0
        var userRarity6Diff = 0
        if (userProfile!!.unitData.hasUnique) {
            val originUniqueLevel = userProfile!!.userData.uniqueLevel
            val willChangeUniqueLevel = userData!!.uniqueLevel
            if (originUniqueLevel > 0 && willChangeUniqueLevel < 1) {
                userUniqueDiff = -1
            }
            if (originUniqueLevel < 1 && willChangeUniqueLevel > 0) {
                userUniqueDiff = 1
            }
        }
        if (userProfile!!.unitData.maxRarity > 5) {
            val originRarity = userProfile!!.userData.rarity
            val willChangeRarity = userData!!.rarity
            if (originRarity > 5 && willChangeRarity < 6) {
                userRarity6Diff = -1
            }
            if (originRarity < 6 && willChangeRarity > 5) {
                userRarity6Diff = 1
            }

        }
        if (userUniqueDiff != 0 || userRarity6Diff != 0) {
            onMaxUserDataChange(userUniqueDiff, userRarity6Diff)
        }

        userProfile!!.userData = userData!!
        originProperty = userProfile!!.getProperty(userData!!)
        saveVisible = false

        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            db.putUserData(userData!!)
        }
    }

    fun destroy() {
        restore()

//        userProfile = null
//        userData = null
    }

    private fun restore() {
        if (
            userProfile != null &&
            backupUnitRarity != null &&
            backupUnitPromotionStatus != null &&
            backupUnitPromotion != null
        ) {
            userProfile!!.unitRarity = backupUnitRarity
            userProfile!!.unitPromotionStatus = backupUnitPromotionStatus
            userProfile!!.unitPromotion = backupUnitPromotion
            backupUnitRarity = null
            backupUnitPromotionStatus = null
            backupUnitPromotion = null
        }
    }

    private fun changeState() {
        saveVisible = userData != userProfile!!.userData
        property = userProfile!!.getProperty(userData!!)
    }

    private suspend fun UserProfile.loadAll(db: AppDatabase) = withContext(defaultDispatcher) {
        val list = awaitAll(
            async {
                db.getUnitRarity(
                    unitData.unitId,
                    userData.rarity
                )
            },
            async {
                db.getUnitPromotionStatus(
                    unitData.unitId,
                    userData.promotionLevel
                )
            },
            async {
                db.getUnitPromotion(
                    unitData.unitId,
                    userData.promotionLevel
                )
            },
            async {
                db.getUniqueData(unitData.equipId)
            },
            async {
                if (charaStoryStatus == null) {
                    db.getCharaStoryStatus(unitData.unitId)
                } else {
                    charaStoryStatus
                }
            },
            async {
                db.getPromotions(unitData.unitId)
            },
            async {
                db.getUnitAttackPatternList(unitData.unitId)
            },
            async {
                db.getUnitSkillData(unitData.unitId)
            },
            async {
                db.getExSkillData(unitData.unitId)
            },
            async {
                db.getPromotionBonusList(unitData.unitId)
            },
            async {
                unitConversionData?.let {
                    db.getUnitAttackPatternList(it.convertedUnitId)
                }
            },
            async {
                unitConversionData?.let {
                    db.getUnitSkillData(it.convertedUnitId)
                }
            },
            async {
                unitConversionData?.let {
                    db.getExSkillData(it.convertedUnitId)
                }
            }
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
    }

    private suspend fun UserProfile.loadStory(db: AppDatabase) = withContext(defaultDispatcher) {
        if (charaStoryStatus == null) {
            charaStoryStatus = db.getCharaStoryStatus(unitData.unitId)
        }
    }
}
