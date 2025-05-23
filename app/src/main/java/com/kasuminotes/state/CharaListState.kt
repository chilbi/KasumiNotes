package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.Element
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.common.Role
import com.kasuminotes.data.UserData
import com.kasuminotes.data.UserProfile

class CharaListState {
    var backupProfiles: List<UserProfile>? = null
    private var backSearchText: String? = null

    var profiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set
    var derivedProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set
    var searchText by mutableStateOf("")
        private set
    var element by mutableStateOf(Element.All)
        private set
    var role by mutableStateOf(Role.All)
        private set
    var atkType by mutableStateOf(AtkType.All)
        private set
    var position by mutableStateOf(Position.All)
        private set
    var orderBy by mutableStateOf(OrderBy.StartTime)
        private set
    var sortDesc by mutableStateOf(true)
        private set
    var rarity6 by mutableStateOf(false)
        private set
    var unique1 by mutableStateOf(false)
        private set
    var unique2 by mutableStateOf(false)
        private set
    var lockedChara by mutableStateOf<List<Int>>(emptyList())
        private set
    var derivedLockedChara by mutableStateOf<List<Int>>(emptyList())
        private set
    var selectedChara by mutableStateOf<List<Int>>(emptyList())
        private set

//    private var loadedCount = 0
//    private var isLoading = false
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
            val newUnique1Level = if (uniqueLevel == null) {
                userData.unique1Level
            } else {
                if (unitData.hasUnique1) uniqueLevel else 0
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
                newUnique1Level,
                userData.unique2Level,
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
                userData.subPercentJson,
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

    fun changeElement(value: Element) {
        element = value
        derived()
    }

    fun changeRole(value: Role) {
        role = value
        derived()
    }

    fun changeOrderBy(value: OrderBy) {
        if (value == orderBy) {
            sortDesc = !sortDesc
            derivedProfiles = derivedProfiles.reversed()
        } else {
            orderBy = value
            if (!sortDesc) sortDesc = true
            sort(derivedProfiles, value, true)
        }
    }

    fun toggleRarity6() {
        if (rarity6) {
            rarity6 = false
        } else {
            rarity6 = true
            unique1 = false
            unique2 = false
        }
        derived()
    }

    fun toggleUnique1() {
        if (unique1) {
            unique1 = false
        } else {
            rarity6 = false
            unique1 = true
            unique2 = false
        }
        derived()
    }

    fun toggleUnique2() {
        if (unique2) {
            unique2 = false
        } else {
            rarity6 = false
            unique1 = false
            unique2 = true
        }
        derived()
    }

    private fun derived() {
        val originProfiles = profiles
        val list = if (
            searchText.isEmpty() &&
            element == Element.All &&
            role == Role.All &&
            atkType == AtkType.All &&
            position == Position.All &&
            !rarity6 &&
            !unique1 &&
            !unique2
        ) {
            originProfiles
        } else {
            originProfiles.filter { userProfile ->
                userProfile.getRealUnitData(userProfile.userData.rarity).let {
                    val rarity6Match = !rarity6 || it.maxRarity == 6
                    val unique1Match = !unique1 || it.hasUnique1
                    val unique2Match = !unique2 || it.hasUnique2
                    val elementMatch = element == Element.All || element.ordinal == it.talentId
                    val roleMatch = role == Role.All || role.ordinal == it.unitRoleId
                    val atkTypeMatch = atkType == AtkType.All || atkType.ordinal == it.atkType
                    val positionMatch = position == Position.All || position.ordinal == it.position
                    val searchTextMatch = searchText.isEmpty() ||
                            (it.unitId.toString() + it.unitName + it.kana + it.actualName)
                                .contains(searchText)

                    rarity6Match && unique1Match && unique2Match && elementMatch && roleMatch &&
                            atkTypeMatch && positionMatch && searchTextMatch
                }
            }
        }
        derivedLocked(list)
        sort(list, orderBy, sortDesc)
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

    private fun sort(list: List<UserProfile>, order: OrderBy, desc: Boolean) {
        derivedProfiles = if (desc) list.sortedByDescending { it.getIntOf(order) }
        else list.sortedBy { it.getIntOf(order) }
        /*if (isLoaded) {
            derivedProfiles = if (desc) list.sortedByDescending { it.getIntOf(order) }
            else list.sortedBy { it.getIntOf(order) }
        } else {
            when (order) {
                OrderBy.StartTime,
                OrderBy.CharaId,
                OrderBy.Rarity,
                OrderBy.SearchAreaWidth,
                OrderBy.Age,
                OrderBy.Height,
                OrderBy.Weight -> {
                    derivedProfiles = if (desc) list.sortedByDescending { it.getIntOf(order) }
                    else list.sortedBy { it.getIntOf(order) }
                }
                else -> {
                    if (isLoading) return
                    scope.launch(Dispatchers.IO) {
                        isLoading = true
                        val db = appRepository.getDatabase()
                        profiles.map {
                            it.load(db, profiles)
                            val property = it.getProperty(it.userData)

                            val exSkillProperty = it.getExSkillProperty(it.userData)
                            val baseProperty = Property { i -> property[i] - exSkillProperty[i] }

                            val exEquipProperty = it.getExEquipProperty(baseProperty, it.userData)
                            val includeExEquipProperty = Property { i -> exEquipProperty[i] + property[i] }
                            it.setProperty(property, baseProperty, includeExEquipProperty)
                            loadedCount += 1
                        }
                        isLoading = false
                        isLoaded = true
                        derivedProfiles = if (desc) list.sortedByDescending { it.getIntOf(order) }
                        else list.sortedBy { it.getIntOf(order) }
                    }
                }
            }
        }*/
    }
}
