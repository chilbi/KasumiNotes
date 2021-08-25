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
    var selectedChara by mutableStateOf<List<Int>>(emptyList())
        private set

    fun changeToImages(allUserProfile: List<UserProfile>) {
        backupProfiles = profiles

        changeProfiles(allUserProfile)
    }

    fun changeToEditor(allUserProfile: List<UserProfile>, unlockedProfiles: List<UserProfile>) {
        backupProfiles = profiles

        when {
            unlockedProfiles.isEmpty() -> {
                changeProfiles(allUserProfile)
                lockedChara = allUserProfile.map { it.unitData.unitId }
            }
            unlockedProfiles.size == allUserProfile.size -> {
                val all = unlockedProfiles.map { userProfile ->
                    UserProfile(userProfile.userData.copy(userId = 0), userProfile.unitData)
                }
                changeProfiles(all)
                lockedChara = emptyList()
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
                changeProfiles(all)
                lockedChara = locked
            }
        }
    }

    fun destroy() {
        if (backupProfiles != null) {
            destroy(backupProfiles!!)
        }
    }

    fun destroy(newProfiles: List<UserProfile>) {
        changeProfiles(newProfiles)
        backupProfiles = null

        lockedChara = emptyList()
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
        selectedChara = emptyList()
    }

    fun selectAllChara() {
        selectedChara = profiles.map { it.unitData.unitId }
    }

    fun selectUnlockedChara() {
        val newSelected = mutableListOf<Int>()
        val locked = lockedChara
        profiles.forEach { userProfile ->
            val unitId = userProfile.unitData.unitId
            if (!locked.contains(unitId)) {
                newSelected.add(unitId)
            }
        }
        selectedChara = newSelected
    }

    fun selectLockedChara() {
        selectedChara = lockedChara
    }

    fun deleteProfiles() {
        val newLocked = lockedChara.toMutableList()
        selectedChara.forEach { unitId ->
            if (!newLocked.contains(unitId)) {
                newLocked.add(unitId)
            }
        }
        lockedChara = newLocked
        selectedChara = emptyList()
    }

    fun modifyProfiles(
        rarity: Int,
        charaLevel: Int,
        loveLevel: Int,
        uniqueLevel: Int,
        promotionLevel: Int,
        unlockSlot: Int
    ) {
        var equip1Level = -1
        var equip2Level = -1
        var equip3Level = -1
        var equip4Level = -1
        var equip5Level = -1
        var equip6Level = -1

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

        val newLocked = lockedChara.toMutableList()
        val newProfiles = profiles.toMutableList()
        selectedChara.forEach { unitId ->
            if (lockedChara.contains(unitId)) {
                newLocked.remove(unitId)
            }
            val userProfile = newProfiles.find { it.unitData.unitId == unitId }!!
            val userData = userProfile.userData
            val unitData = userProfile.unitData

            val newRarity = if (rarity > unitData.maxRarity) unitData.maxRarity else rarity
            val newLoveLevel = if (unitData.maxRarity < 6 && loveLevel > 8) 8 else loveLevel
            val newUniqueLevel = if (unitData.hasUnique) uniqueLevel else 0

            userProfile.userData = UserData(
                userData.userId,
                userData.unitId,
                newRarity,
                charaLevel,
                newLoveLevel,
                newUniqueLevel,
                promotionLevel,
                charaLevel,
                charaLevel,
                charaLevel,
                charaLevel,
                equip1Level,
                equip2Level,
                equip3Level,
                equip4Level,
                equip5Level,
                equip6Level
            )
        }
        profiles = newProfiles
        lockedChara = newLocked
        selectedChara = emptyList()
    }

    fun changeProfiles(value: List<UserProfile>) {
        profiles = value
        derivedProfiles = derived(profiles)
    }

    fun changeSearchText(value: String) {
        searchText = value
        derivedProfiles = derived(profiles)
    }

    fun changeAtkType(value: AtkType) {
        atkType = value
        derivedProfiles = derived(profiles)
    }

    fun changePosition(value: Position) {
        position = value
        derivedProfiles = derived(profiles)
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

    private fun derived(originProfiles: List<UserProfile>): List<UserProfile> {
        val list =
            if (searchText.isEmpty() && atkType == AtkType.All && position == Position.All) {
                originProfiles
            } else {
                originProfiles.filter { data ->
                    data.unitData.let {
                        val searchTextMatch = searchText.isEmpty() || (
                            it.unitId.toString() + it.unitName + it.kana + it.actualName
                            ).contains(searchText)
                        val atkTypeMatch =
                            atkType == AtkType.All || atkType.ordinal == it.atkType
                        val positionMatch =
                            position == Position.All || position.ordinal == it.position
                        searchTextMatch && atkTypeMatch && positionMatch
                    }
                }
            }
        return sort(list, orderBy, sortDesc)
    }

    private fun sort(list: List<UserProfile>, order: OrderBy, desc: Boolean): List<UserProfile> {
        val selector: (UserProfile) -> Int? = { data ->
            when (order) {
                OrderBy.StartTime -> data.unitData.startTimeInt
                OrderBy.ID -> data.unitData.unitId
                OrderBy.Rarity -> data.userData.rarity
                OrderBy.SearchAreaWidth -> data.unitData.searchAreaWidth
                OrderBy.Age -> data.unitData.age.toIntOrNull() ?: 9999
                OrderBy.Height -> data.unitData.height.toIntOrNull() ?: 9999
                OrderBy.Weight -> data.unitData.weight.toIntOrNull() ?: 9999
            }
        }
        return if (desc) {
            list.sortedByDescending(selector)
        } else {
            list.sortedBy(selector)
        }
    }
}
