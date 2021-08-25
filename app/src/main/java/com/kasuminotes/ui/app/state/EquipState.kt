package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.QuestRange
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.EquipCraft
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.Property
import com.kasuminotes.data.QuestData
import com.kasuminotes.data.UniqueCraft
import com.kasuminotes.data.UniqueData
import com.kasuminotes.db.getDropRangeMap
import com.kasuminotes.db.getEquipCraft
import com.kasuminotes.db.getEquipData
import com.kasuminotes.db.getQuestDataList
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class EquipState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private var dropRangeMap: Map<Int, QuestRange>? = null
    private var onEnhanceLevelChange: ((Int) -> Unit)? = null

    var equipData by mutableStateOf<EquipData?>(null)
        private set
    var uniqueData by mutableStateOf<UniqueData?>(null)
        private set
    var property by mutableStateOf(Property())
        private set
    var enhanceLevel by mutableStateOf(0)
        private set
    var maxEnhanceLevel by mutableStateOf(0)
        private set
    var equipCraftList by mutableStateOf<List<EquipCraft>?>(null)
        private set
    var uniqueCraftList by mutableStateOf<List<UniqueCraft>?>(null)
        private set
    var searchList by mutableStateOf<List<Int>?>(null)
        private set
    var questTypes by mutableStateOf(QuestType.values())
        private set
    var sortDesc by mutableStateOf(true)
        private set
    var min37 by mutableStateOf(false)
        private set
    var questDataList by mutableStateOf<List<QuestData>?>(null)
        private set

    suspend fun selectEquip(maxArea: Int, equipId: Int) {
        val db = appRepository.getDatabase()
        val value = db.getEquipData(equipId)
        selectEquipData(maxArea, value)
    }

    fun selectEquipData(
        maxArea: Int,
        value: EquipData,
        originEnhanceLevel: Int = value.maxEnhanceLevel,
        onLevelChange: ((Int) -> Unit)? = null
    ) {
        if (min37 && maxArea < 37) {
            min37 = false
        }
        equipData = value
        uniqueData = null
        enhanceLevel = originEnhanceLevel
        maxEnhanceLevel = value.maxEnhanceLevel
        onEnhanceLevelChange = onLevelChange
        property = value.getProperty(originEnhanceLevel)
        if (value.craftFlg == 1) {
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()
                val equipCraft = db.getEquipCraft(value.equipmentId)
                equipCraftList = equipCraft.getCraftList()
            }
        } else {
            equipCraftList = listOf(EquipCraft(value.equipmentId, 1, null))
        }
    }

    fun selectUniqueData(
        value: UniqueData,
        originEnhanceLevel: Int,
        maxUniqueLevel: Int,
        onLevelChange: (Int) -> Unit
    ) {
        uniqueData = value
        equipData = null
        enhanceLevel = originEnhanceLevel
        maxEnhanceLevel = maxUniqueLevel
        onEnhanceLevelChange = onLevelChange
        property = value.getProperty(originEnhanceLevel)
        changeUniqueCraft(value.equipmentId, maxUniqueLevel)
    }

    fun changeEnhanceLevel(value: Int) {
        enhanceLevel = value
        if (equipData != null) {
            property = equipData!!.getProperty(value)
        } else if (uniqueData != null) {
            property = uniqueData!!.getProperty(value)
        }
        onEnhanceLevelChange?.invoke(value)
    }

    fun changeSearchList(materialId: Int) {
        val searchedList: List<Int>? = if (searchList == null) {
            listOf(materialId)
        } else {
            if (searchList!!.contains(materialId)) {
                if (searchList!!.size == 1) {
                    null
                } else {
                    searchList!!.filter { it != materialId }
                }
            } else {
                val list = searchList!!.toMutableList()
                list.add(materialId)
                list
            }
        }

        if (searchedList == null) {
            searchList = null
            questDataList = null
        } else {
            searchList = searchedList
            changeQuestDataList(searchedList)
        }
    }

    fun changeQuestTypes(type: QuestType) {
        questTypes = if (questTypes.contains(type)) {
            questTypes.filter { it != type }.toTypedArray()
        } else {
            questTypes.plus(type)
        }
        if (searchList != null) {
            changeQuestDataList(searchList!!)
        }
    }

    fun toggleSortDesc() {
        sortDesc = !sortDesc
        if (questDataList != null) {
            questDataList = if (sortDesc) {
                questDataList!!.sortedByDescending { it.questId }
            } else {
                questDataList!!.sortedBy { it.questId }
            }
        }
    }

    fun toggleMin37() {
        min37 = !min37
        if (searchList != null) {
            changeQuestDataList(searchList!!)
        }
    }

    fun destroy() {
//        dropRangeMap = null
//        equipData = null
//        uniqueData = null
        equipCraftList = null
        uniqueCraftList = null
        searchList = null
        questDataList = null
        onEnhanceLevelChange = null
    }

    private fun changeUniqueCraft(equipmentId: Int, maxUnlockLevel: Int) {
        val memoryId = "31${equipmentId.toString().substring(2, 5)}".toInt()
        val list = mutableListOf<UniqueCraft>()
        list.add(UniqueCraft(30, 140000, 3, memoryId, 50))
        list.add(UniqueCraft(50, 140001, 5, memoryId, 10))
        list.add(UniqueCraft(70, 140001, 5, memoryId, 10))
        list.add(UniqueCraft(90, 140001, 8, memoryId, 10))
        list.add(UniqueCraft(110, 140001, 10, memoryId, 10))
        list.add(UniqueCraft(130, 140001, 10, memoryId, 10))
        var unlockLevel = 140
        while (unlockLevel <= maxUnlockLevel) {
            list.add(UniqueCraft(unlockLevel, 140001, 10, memoryId, 15))
            unlockLevel += 10
        }
        uniqueCraftList = list
    }

    private fun changeQuestDataList(searchedList: List<Int>) {
        if (questTypes.isEmpty()) {
            questDataList = emptyList()
        } else {
            questDataList = null
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()

                if (dropRangeMap == null) {
                    dropRangeMap = db.getDropRangeMap()
                }

                val questRangeList = QuestRange.getQuestRangeList(
                    searchedList,
                    questTypes,
                    dropRangeMap!!,
                    min37
                )

                if (questRangeList.isEmpty()) {
                    questDataList = emptyList()
                } else {
                    val lists = questRangeList.map { item ->
                        async { db.getQuestDataList(item) }
                    }.awaitAll()

                    val resultList = mutableListOf<QuestData>()

                    for (list in lists) {
                        for (item in list) {
                            if (searchedList.any { item.contains(it) }) {
                                resultList.add(item)
                            }
                        }
                    }
                    questDataList = if (sortDesc) {
                        resultList.sortedByDescending { it.questId }
                    } else {
                        resultList.sortedBy { it.questId }
                    }
                }
            }
        }
    }
}
