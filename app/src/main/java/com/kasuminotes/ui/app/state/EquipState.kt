package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.kasuminotes.db.getEquipCraft
import com.kasuminotes.db.getEquipData
import com.kasuminotes.db.getQuestDataList
import com.kasuminotes.db.getUnique2Craft
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

class EquipState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
//    private var dropRangeMap: Map<Int, QuestRange>? = null
    private var allQuestDataList = emptyList<QuestData>()
    private var onEnhanceLevelChange: ((value: Int, slot: Int) -> Unit)? = null

    var equipData by mutableStateOf<EquipData?>(null)
        private set
    var unique1Data by mutableStateOf<UniqueData?>(null)
        private set
    var unique2Data by mutableStateOf<UniqueData?>(null)
        private set
    var property by mutableStateOf(Property.zero)
        private set
    var enhanceLevel by mutableIntStateOf(0)
        private set
    var maxEnhanceLevel by mutableIntStateOf(0)
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

    fun initEquip(maxArea: Int, equipId: Int) {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            val equipDataValue = db.getEquipData(equipId)
            initEquipData(equipDataValue, maxArea)
        }
    }

    fun initEquipData(
        equipDataValue: EquipData,
        maxArea: Int,
        originEnhanceLevel: Int = equipDataValue.maxEnhanceLevel,
        onLevelChange: ((value: Int, slot: Int) -> Unit)? = null
    ) {
        if (min37 && maxArea < 37) {
            min37 = false
        }
        equipData = equipDataValue
        unique1Data = null
        unique2Data = null
        enhanceLevel = originEnhanceLevel
        maxEnhanceLevel = equipDataValue.maxEnhanceLevel
        onEnhanceLevelChange = onLevelChange
        property = equipDataValue.getProperty(originEnhanceLevel)
        if (equipDataValue.craftFlg == 1) {
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()
                val equipCraft = db.getEquipCraft(equipDataValue.equipmentId)
                equipCraftList = equipCraft.getCraftList()
            }
        } else {
            equipCraftList = listOf(EquipCraft(equipDataValue.equipmentId, 1, null))
        }
    }

    fun initUnique1Data(
        uniqueDataValue: UniqueData,
        originEnhanceLevel: Int,
        maxUniqueLevel: Int,
        onLevelChange: (value: Int, slot: Int) -> Unit
    ) {
        equipData = null
        unique1Data = uniqueDataValue
        unique2Data = null
        enhanceLevel = originEnhanceLevel
        maxEnhanceLevel = maxUniqueLevel
        onEnhanceLevelChange = onLevelChange
        property = uniqueDataValue.getUnique1Property(originEnhanceLevel)
        changeUnique1Craft(uniqueDataValue.equipmentId, maxUniqueLevel)
    }

    fun initUnique2Data(
        uniqueDataValue: UniqueData,
        originEnhanceLevel: Int,
        onLevelChange: (value: Int, slot: Int) -> Unit
    ) {
        equipData = null
        unique1Data = null
        unique2Data = uniqueDataValue
        enhanceLevel = originEnhanceLevel
        maxEnhanceLevel = 5
        onEnhanceLevelChange = onLevelChange
        property = uniqueDataValue.getUnique2Property(originEnhanceLevel)
        changeUnique2Craft(uniqueDataValue.equipmentId)
    }

    fun changeEnhanceLevel(value: Int) {
        enhanceLevel = value
        if (equipData != null) {
            property = equipData!!.getProperty(value)
            onEnhanceLevelChange?.invoke(value, 0)
        } else if (unique1Data != null) {
            property = unique1Data!!.getUnique1Property(value)
            onEnhanceLevelChange?.invoke(value, 1)
        } else if (unique2Data != null) {
            property = unique2Data!!.getUnique2Property(value)
            onEnhanceLevelChange?.invoke(value, 2)
        }
    }

    fun changeSearchList(materialId: Int) {
        var hasChanged = true
        val searchedList: List<Int>? = if (searchList == null) {
            listOf(materialId)
        } else {
            if (searchList!!.contains(materialId)) {
                if (searchList!!.size == 1) {
                    null
                } else {
                    searchList!!.filter { it != materialId }
                }
            } else if (searchList!!.size < 3) {// TODO 提示最多选3个
                val list = searchList!!.toMutableList()
                list.add(materialId)
                list
            } else {
                hasChanged = false
                searchList
            }
        }
        if (hasChanged) {
            if (searchedList == null) {
                searchList = null
                questDataList = null
            } else {
                searchList = searchedList
                changeQuestDataList(searchedList)
            }
        }
    }

    fun changeQuestTypes(type: QuestType) {
        questTypes = if (questTypes.contains(type)) {
            questTypes.filter { it != type }.toTypedArray()
        } else {
            questTypes.plus(type)
        }
        if (searchList != null && allQuestDataList.isNotEmpty()) {
            questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, questTypes, min37)
        }
    }

    fun toggleMin37() {
        min37 = !min37
        if (searchList != null && allQuestDataList.isNotEmpty()) {
            questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, questTypes, min37)
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

    fun destroy() {
//        dropRangeMap = null
//        equipData = null
//        unique1Data = null
        equipCraftList = null
        uniqueCraftList = null
        searchList = null
        questDataList = null
        onEnhanceLevelChange = null
    }

    private fun changeUnique1Craft(equipmentId: Int, maxUnlockLevel: Int) {
        val memoryId = "31${equipmentId.toString().substring(2, 5)}".toInt()
        val list = mutableListOf<UniqueCraft>()
        list.add(UniqueCraft(30, 140000, 3, memoryId, 50))
        list.add(UniqueCraft(50, 140001, 5, memoryId, 10))
        list.add(UniqueCraft(70, 140001, 5, memoryId, 10))
        list.add(UniqueCraft(90, 140001, 8, memoryId, 10))
        list.add(UniqueCraft(110, 140001, 10, memoryId, 10))
        list.add(UniqueCraft(130, 140001, 10, memoryId, 10))
        var unlockLevel = 140
        val craft15Level = min(maxUnlockLevel, 230)
        while (unlockLevel <= craft15Level) {
            list.add(UniqueCraft(unlockLevel, 140001, 10, memoryId, 15))
            unlockLevel += 10
        }
        if (maxUnlockLevel > 230) {
            while (unlockLevel <= maxUnlockLevel) {
                list.add(UniqueCraft(unlockLevel, 140001, 10, memoryId, 5))
                unlockLevel += 10
            }
        }
        uniqueCraftList = list
    }

    private fun changeUnique2Craft(equipmentId: Int) {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            val memoryId = db.getUnique2Craft(equipmentId)
            val list = mutableListOf<UniqueCraft>()
            list.add(UniqueCraft(0, -1, -1, memoryId, 50))
            list.add(UniqueCraft(1, -1, -1, memoryId, 10))
            list.add(UniqueCraft(2, -1, -1, memoryId, 15))
            list.add(UniqueCraft(3, -1, -1, memoryId, 20))
            list.add(UniqueCraft(4, -1, -1, memoryId, 25))
            list.add(UniqueCraft(5, -1, -1, memoryId, 30))
            uniqueCraftList = list
        }
    }

    private fun changeQuestDataList(searchedList: List<Int>) {
        if (questTypes.isEmpty()) {
            questDataList = emptyList()
        } else {
            questDataList = null
            scope.launch(defaultDispatcher) {
                val db = appRepository.getDatabase()
                allQuestDataList = db.getQuestDataList(searchedList, sortDesc)
                questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, questTypes, min37)
//                if (dropRangeMap == null) {
//                    dropRangeMap = db.getDropRangeMap()
//                }
//
//                val questRangeList = QuestRange.getQuestRangeList(
//                    searchedList,
//                    questTypes,
//                    dropRangeMap!!,
//                    min37
//                )
//
//                if (questRangeList.isEmpty()) {
//                    questDataList = emptyList()
//                } else {
//                    val lists = questRangeList.map { item ->
//                        async { db.getQuestDataList(item) }
//                    }.awaitAll()
//
//                    val resultList = mutableListOf<QuestData>()
//
//                    for (list in lists) {
//                        for (item in list) {
//                            if (searchedList.any { item.contains(it) }) {
//                                resultList.add(item)
//                            }
//                        }
//                    }
//                    questDataList = if (sortDesc) {
//                        resultList.sortedByDescending { it.questId }
//                    } else {
//                        resultList.sortedBy { it.questId }
//                    }
//                }
            }
        }
    }
}
