package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.Label
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.Property
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UnitPromotionStatus
import com.kasuminotes.data.UnitRarity
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile
import com.kasuminotes.db.getUnitPromotion
import com.kasuminotes.db.getUnitPromotionStatus
import com.kasuminotes.db.getUnitRarity
import com.kasuminotes.db.putUserData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharaState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private var onMaxUserDataChange: (userUniqueDiff: Int, userRarity6Diff: Int) -> Unit
) {
    private var backupUnitRarity: UnitRarity? = null
    private var backupUnitPromotionStatus: UnitPromotionStatus? = null
    private var backupUnitPromotion: UnitPromotion? = null
    private var backupExEquipSlots: List<ExEquipSlot> = emptyList()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var userData by mutableStateOf<UserData?>(null)
        private set
    var rankBonusProperty by mutableStateOf<Property?>(null)
        private set
    var exSkillProperty by mutableStateOf(Property.zero)
        private set
    //rarity+promotionStatus+promotion+unique1&2+story+bonus
    var baseProperty by mutableStateOf(Property.zero)
        private set
    //base+exSkill+exEquip+exEquipSkill
    var totalProperty by mutableStateOf(Property.zero)
        private set
    var saveVisible by mutableStateOf(false)
        private set

    fun initUserProfile(profile: UserProfile, profiles: List<UserProfile>, maxCharaLevel: Int) {
        restore()
        if (profile.userData.charaLevel > maxCharaLevel) {
            profile.userData = profile.userData.copy(lvLimitBreak = 10)
        }
        userProfile = profile
        userData = profile.userData
        saveVisible = false

        if (profile.baseProperty != null) {
            initData(profile)
        } else {
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                profile.load(db, profiles)

                initData(profile)
            }
        }
    }

    fun changeSubPercentList(slotNum: Int, subPercentList: List<Pair<Int, Double>>) {
        val map = userData!!.subPercentMap.toMutableMap()
        map[slotNum] = subPercentList
        val json = UserData.toSubPercentJson(map)
        userData = userData!!.copy(subPercentJson = json)
        changeState()
    }

    fun changeExEquip(slotNum: Int, exEquip: ExEquipData?) {
        val exEquipId = exEquip?.exEquipmentId ?: 0
        val update: (Int) -> List<ExEquipSlot> = { index: Int ->
            userProfile!!.exEquipSlots.mapIndexed { i, slot ->
                if (i == index) slot.copy(exEquipData = exEquip)
                else slot
            }
        }
        userData = when (slotNum) {
            1 -> {
                userProfile!!.exEquipSlots = update(0)
                userData!!.copy(exEquip1 = exEquipId)
            }
            2 -> {
                userProfile!!.exEquipSlots = update(1)
                userData!!.copy(exEquip2 = exEquipId)
            }
            else -> {
                userProfile!!.exEquipSlots = update(2)
                userData!!.copy(exEquip3 = exEquipId)
            }

        }
        changeState()
    }

    fun changeExEquipLevel(slotNum: Int, level: Int) {
        userData = when (slotNum) {
            1 -> userData!!.copy(exEquip1Level = level)
            2 -> userData!!.copy(exEquip2Level = level)
            else -> userData!!.copy(exEquip3Level = level)
        }
        changeState()
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
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                val unitRarity = db.getUnitRarity(userProfile!!.unitData.unitId, value)
                userProfile!!.unitRarity = unitRarity
                userData = userData!!.copy(rarity = value)
                changeState()
            }
        }
    }

    fun changeUniqueLevel(value: Int, slot: Int) {
        userData = if (slot == 1) userData!!.copy(unique1Level = value)
        else userData!!.copy(unique2Level = value)
        changeState()
    }

    fun changeLoveLevel(value: Int) {
        userData = userData!!.copy(loveLevel = value)
        changeState()
    }

    fun changePromotionLevel(value: Int) {
        if (value != userData!!.promotionLevel) {
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                val unitPromotionStatus = db.getUnitPromotionStatus(userProfile!!.unitData.unitId, value)
//                val promotions = userProfile!!.promotions
//                val unitPromotion = promotions[promotions.size - value]
                val unitPromotion = db.getUnitPromotion(userProfile!!.unitData.unitId, value)
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
                rankBonusProperty = userProfile!!.getRankBonusProperty(value)
                changeState()
            }
        }
    }

    fun changeSkillLevel(value: Int, labelText: String) {
        userData = when (labelText) {
            Label.ub -> userData!!.copy(ubLevel = value)
            Label.ex -> userData!!.copy(exLevel = value)
            Label.skill + "1" -> userData!!.copy(skill1Level = value)
            else -> userData!!.copy(skill2Level = value)
        }
        changeState()
    }

    fun changeEquipLevel(value: Int, slot: Int) {
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
        userProfile!!.exEquipSlots = backupExEquipSlots

        userData = userProfile!!.userData
        rankBonusProperty = userProfile!!.getRankBonusProperty(userData!!.promotionLevel)
        changeState()
    }

    fun save() {
        backupUnitRarity = userProfile!!.unitRarity
        backupUnitPromotionStatus = userProfile!!.unitPromotionStatus
        backupUnitPromotion = userProfile!!.unitPromotion
        backupExEquipSlots = userProfile!!.exEquipSlots

        var userUniqueDiff = 0
        var userRarity6Diff = 0
        if (userProfile!!.unitData.hasUnique1) {
            val originUniqueLevel = userProfile!!.userData.unique1Level
            val willChangeUniqueLevel = userData!!.unique1Level
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
        userProfile!!.setProperty(baseProperty, totalProperty)
        saveVisible = false

        scope.launch(Dispatchers.IO) {
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
            userProfile!!.exEquipSlots = backupExEquipSlots
            backupUnitRarity = null
            backupUnitPromotionStatus = null
            backupUnitPromotion = null
            backupExEquipSlots = emptyList()
        }
    }

    private fun initData(profile: UserProfile) {
        backupUnitRarity = profile.unitRarity
        backupUnitPromotionStatus = profile.unitPromotionStatus
        backupUnitPromotion = profile.unitPromotion
        backupExEquipSlots = profile.exEquipSlots

        rankBonusProperty = profile.getRankBonusProperty(userData!!.promotionLevel)
        calcProperty()
        profile.setProperty(baseProperty, totalProperty)
    }

    //base+exSkill+exEquip+exEquipSkill
    private fun calcProperty() {
        val base = userProfile!!.getBaseProperty(userData!!)
        val exSkill = userProfile!!.getExSkillProperty(userData!!)
        val exEquip = userProfile!!.getExEquipProperty(base, userData!!)
        val exEquipSkill = userProfile!!.getExEquipSkillProperty(base, exSkill, exEquip, userProfile!!.unitData.talentId)
        exSkillProperty = exSkill
        baseProperty = base
        totalProperty = Property { i -> base[i] + exSkill[i] + exEquip[i] + exEquipSkill[i] }
    }

    private fun changeState() {
        saveVisible = userData != userProfile!!.userData
        calcProperty()
    }
}
