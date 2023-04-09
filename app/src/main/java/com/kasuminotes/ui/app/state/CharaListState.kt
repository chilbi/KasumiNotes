package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile

class CharaListState {
    var backupProfiles: List<UserProfile>? = null
    var backSearchText: String? = null

    var profiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set
    var derivedProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set
    var searchText by mutableStateOf("")
        private set
    var atkType by mutableStateOf(AtkType.All)
        private set
    var position by mutableStateOf(Position.All)
        private set
    var orderBy by mutableStateOf(OrderBy.StartTime)
        private set
    var sortDesc by mutableStateOf(true)
        private set
    var lockedChara by mutableStateOf<List<Int>>(emptyList())
        private set
    var derivedLockedChara by mutableStateOf<List<Int>>(emptyList())
        private set
    var selectedChara by mutableStateOf<List<Int>>(emptyList())
        private set

//    private var isLoaded = false

    fun initImages(allUserProfile: List<UserProfile>) {
        backupProfiles = profiles
        backSearchText = searchText

        if (searchText != "") searchText = ""
        changeProfiles(allUserProfile)
    }

    fun initEditor(allUserProfile: List<UserProfile>, unlockedProfiles: List<UserProfile>) {
        backupProfiles = profiles
        backSearchText = searchText

        if (searchText != "") searchText = ""
        when {
            unlockedProfiles.isEmpty() -> {
                lockedChara = allUserProfile.map { it.unitData.unitId }
                changeProfiles(allUserProfile)
            }
            unlockedProfiles.size == allUserProfile.size -> {
                val all = unlockedProfiles.map { userProfile ->
                    UserProfile(userProfile.userData.copy(userId = 0), userProfile.unitData)
                }
                lockedChara = emptyList()
                changeProfiles(all)
            }
            else -> {
                val all = mutableListOf<UserProfile>()
                val locked = mutableListOf<Int>()
                allUserProfile.forEach { userProfile ->
                    val unitId = userProfile.unitData.unitId
                    val unlockedProfile = unlockedProfiles.find { it.unitData.unitId == unitId }
                    if (unlockedProfile == null) {
                        all.add(userProfile)
                        locked.add(unitId)
                    } else {
                        all.add(UserProfile(unlockedProfile.userData.copy(userId = 0), unlockedProfile.unitData))
                    }
                }
                lockedChara = locked
                changeProfiles(all)
            }
        }
    }

    fun destroy() {
        if (backupProfiles != null) {
            destroy(backupProfiles!!)
        }
    }

    fun destroy(newProfiles: List<UserProfile>) {
        if (backSearchText != null && backSearchText != searchText) {
            searchText = backSearchText!!
        }
        changeProfiles(newProfiles)
        backupProfiles = null
        backSearchText = null

        lockedChara = emptyList()
        derivedLockedChara = emptyList()
        selectedChara = emptyList()
    }

    fun selectChara(unitId: Int) {
        selectedChara = if (selectedChara.contains(unitId)) {
            selectedChara.minus(unitId)
        } else {
            selectedChara.plus(unitId)
        }
    }

    fun clearSelected() {
        if (selectedChara.isNotEmpty()) {
            selectedChara = emptyList()
        }
    }

    fun selectAllChara() {
        selectedChara = derivedProfiles.map { it.unitData.unitId }
    }

    fun selectUnlockedChara() {
        val newSelected = mutableListOf<Int>()
        val locked = derivedLockedChara
        derivedProfiles.forEach { userProfile ->
            val unitId = userProfile.unitData.unitId
            if (!locked.contains(unitId)) {
                newSelected.add(unitId)
            }
        }
        selectedChara = newSelected
    }

    fun selectLockedChara() {
        selectedChara = derivedLockedChara
    }

    fun deleteProfiles() {
        val newLocked = lockedChara.toMutableList()
        selectedChara.forEach { unitId ->
            if (!newLocked.contains(unitId)) {
                newLocked.add(unitId)
            }
        }
        lockedChara = newLocked
        derivedLocked(derivedProfiles)
    }

    fun modifyProfiles(
        rarity: Int?,
        charaLevel: Int?,
        loveLevel: Int?,
        uniqueLevel: Int?,
        promotionLevel: Int?,
        unlockSlot: Int?
    ) {
        var equip1Level = -1
        var equip2Level = -1
        var equip3Level = -1
        var equip4Level = -1
        var equip5Level = -1
        var equip6Level = -1

        if (unlockSlot != null) {
            if (unlockSlot > 0) {
                equip2Level = 5
            }
            if (unlockSlot > 1) {
                equip4Level = 5
            }
            if (unlockSlot > 2) {
                equip6Level = 5
            }
            if (unlockSlot > 3) {
                equip5Level = 5
            }
            if (unlockSlot > 4) {
                equip3Level = 5
            }
            if (unlockSlot > 5) {
                equip1Level = 5
            }
        }

        val newLocked = lockedChara.toMutableList()
        val newProfiles = profiles.toMutableList()
        selectedChara.forEach { unitId ->
            if (lockedChara.contains(unitId)) {
                newLocked.remove(unitId)
            }
            val userProfile = newProfiles.find { it.unitData.unitId == unitId }!!
            val userData = userProfile.userData
            val unitData = userProfile.unitData

            val newRarity = if (rarity == null) {
                userData.rarity
            } else {
                if (rarity > unitData.maxRarity) unitData.maxRarity else rarity
            }
            val newCharaLevel = charaLevel ?: userData.charaLevel
            val newLoveLevel = if (loveLevel == null) {
                userData.loveLevel
            } else {
                if (unitData.maxRarity < 6 && loveLevel > 8) 8 else loveLevel
            }
            val newUniqueLevel = if (uniqueLevel == null) {
                userData.uniqueLevel
            } else {
                if (unitData.hasUnique) uniqueLevel else 0
            }
            val newPromotionLevel = promotionLevel ?: userData.promotionLevel
            if (unlockSlot == null) {
                equip1Level = userData.equip1Level
                equip2Level = userData.equip2Level
                equip3Level = userData.equip3Level
                equip4Level = userData.equip4Level
                equip5Level = userData.equip5Level
                equip6Level = userData.equip6Level
            }

            userProfile.userData = UserData(
                userData.userId,
                userData.unitId,
                newRarity,
                newCharaLevel,
                newLoveLevel,
                newUniqueLevel,
                newPromotionLevel,
                newCharaLevel,
                newCharaLevel,
                newCharaLevel,
                newCharaLevel,
                equip1Level,
                equip2Level,
                equip3Level,
                equip4Level,
                equip5Level,
                equip6Level,
                userData.exEquip1,
                userData.exEquip2,
                userData.exEquip3,
                userData.exEquip1Level,
                userData.exEquip2Level,
                userData.exEquip3Level,
                userData.lvLimitBreak
            )
        }
        profiles = newProfiles
        lockedChara = newLocked
        derivedLocked(derivedProfiles)
    }

    fun changeProfiles(value: List<UserProfile>) {
//        isLoaded = false
        profiles = value
        derived()
    }

    fun changeSearchText(value: String) {
        searchText = value
        derived()
    }

    fun changeAtkType(value: AtkType) {
        atkType = value
        derived()
    }

    fun changePosition(value: Position) {
        position = value
        derived()
    }

    fun changeOrderBy(value: OrderBy) {
        if (value == orderBy) {
            sortDesc = !sortDesc
            derivedProfiles = derivedProfiles.reversed()
        } else {
            orderBy = value
            if (!sortDesc) sortDesc = true
            derivedProfiles = sort(derivedProfiles, value, true)
        }
    }

    private fun derived() {
        val originProfiles = profiles
        val list =
            if (searchText.isEmpty() && atkType == AtkType.All && position == Position.All) {
                originProfiles
            } else {
                originProfiles.filter { userProfile ->
                    userProfile.getRealUnitData(userProfile.userData.rarity).let {
                        val searchTextMatch = searchText.isEmpty() ||
                                (it.unitId.toString() + it.unitName + it.kana + it.actualName)
                                    .contains(searchText)
                        val atkTypeMatch =
                            atkType == AtkType.All || atkType.ordinal == it.atkType
                        val positionMatch =
                            position == Position.All || position.ordinal == it.position
                        searchTextMatch && atkTypeMatch && positionMatch
                    }
                }
            }
        val sortedList = sort(list, orderBy, sortDesc)
        derivedLocked(list)
        derivedProfiles = sortedList
    }

    private fun derivedLocked(derivedList: List<UserProfile>) {
        if (backupProfiles != null) {
            val originLockedChara = lockedChara
            derivedLockedChara = originLockedChara.filter { unitId ->
                derivedList.any { item -> item.unitData.unitId == unitId }
            }
            clearSelected()
        }
    }

    private fun sort(list: List<UserProfile>, order: OrderBy, desc: Boolean): List<UserProfile> {
        return if (desc) list.sortedByDescending { it.getIntOf(order) }
        else list.sortedBy { it.getIntOf(order) }
//        return if (isLoaded) {
//            if (desc) list.sortedByDescending { it.getIntOf(order) }
//            else list.sortedBy { it.getIntOf(order) }
//        } else {
//            when (order) {
//                OrderBy.StartTime,
//                OrderBy.CharaId,
//                OrderBy.Rarity,
//                OrderBy.SearchAreaWidth,
//                OrderBy.Age,
//                OrderBy.Height,
//                OrderBy.Weight -> {
//                    if (desc) list.sortedByDescending { it.getIntOf(order) }
//                    else list.sortedBy { it.getIntOf(order) }
//                }
//                else -> {
//                    scope.launch(defaultDispatcher) {
//                        val db = appRepository.getDatabase()
//                        profiles.map {
//                            async {
//                                it.load(db, profiles, defaultDispatcher)
//                                it.calcProperty()
//                            }
//                        }.awaitAll()
//                        isLoaded = true
//                        derivedProfiles = sort(list, order, desc)
//                    }
//                    list
//                }
//            }
//        }
    }
}
