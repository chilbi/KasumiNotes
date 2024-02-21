package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.common.QuestMode
import com.kasuminotes.common.QuestRange
import com.kasuminotes.common.QuestType
import com.kasuminotes.data.EquipInfo
import com.kasuminotes.data.QuestData
import com.kasuminotes.db.getEquipMaterialPairList
import com.kasuminotes.db.getEquipmentPairList
import com.kasuminotes.db.getMemoryPieces
import com.kasuminotes.db.getQuestDataList
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuestState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
//    private var dropRangeMap: Map<Int, QuestRange>? = null
    private var allQuestDataList = emptyList<QuestData>()

    var questMode by mutableStateOf(appRepository.getQuestMode())
        private set
    var questType by mutableStateOf(appRepository.getQuestMapType())
        private set
    var area by mutableIntStateOf(appRepository.getQuestMapArea())
        private set
    var searchId by mutableIntStateOf(appRepository.getQuestSearchId())
        private set
    var searchSet by mutableStateOf(appRepository.getQuestSearchSet())
        private set
    var searches by mutableStateOf(searchSet.find { it.first == searchId }!!.second)
        private set
    var searchedList by mutableStateOf(searches.filter { it != 0 })
        private set
    var searchTypes by mutableStateOf(QuestType.entries.toTypedArray())
        private set
    var highlightList by mutableStateOf<List<Int>>(emptyList())
        private set
    var maxArea by mutableIntStateOf(0)
        private set
    var visitIndex by mutableIntStateOf(0)
        private set
    var sortDesc by mutableStateOf(true)
        private set
    var questDataList by mutableStateOf<List<QuestData>?>(null)
        private set
    var equipmentPairList by mutableStateOf<List<Pair<Int, List<EquipInfo>>>?>(null)
        private set
    var typePairList by mutableStateOf<List<Pair<Int, String>>?>(null)
        private set
    var equipTypes by mutableStateOf<Set<Int>>(emptySet())
        private set
    var min37 by mutableStateOf(false)
        private set
    var equipMaterialPairList by mutableStateOf<List<Pair<Int, List<EquipInfo>>>?>(null)
        private set
    var memoryPieces by mutableStateOf<Array<List<Int>>?>(null)
        private set

    fun initQuest(maxArea: Int) {
        destroy()
        this.maxArea = maxArea
        if (min37 && maxArea < 37) {
            min37 = false
        }
        if (area < 1 || area > maxArea) {
            area = maxArea
        }
        when (questMode) {
            QuestMode.Equip -> {
                changeEquipInfoPairList()
            }
            QuestMode.Search -> {
                if (searches.find { it != 0 } != null) {
                    visitIndex = 0
                    changeSearchQuestDataList()
                } else {
                    visitIndex = 1
                    if (equipMaterialPairList == null || memoryPieces == null) {
                        changeMaterialPieces()
                    }
                }
            }
            QuestMode.Map -> {
                changeQuestDataList()
            }
        }
    }

    fun changeQuestMode(mode: QuestMode) {
        if (mode != questMode) {
            when (mode) {
                QuestMode.Map -> {
                    if (questDataList == null || questMode == QuestMode.Search) {
                        changeQuestDataList()
                    }
                }
                QuestMode.Search -> {
                    if (visitIndex == 0) {
                        if (searches.find { it != 0 } != null) {
                            changeSearchQuestDataList()
                        } else {
                            visitIndex = 1
                            if (equipMaterialPairList == null || memoryPieces == null) {
                                changeMaterialPieces()
                            }
                        }
                    } else {
                        if (equipMaterialPairList == null || memoryPieces == null) {
                            changeMaterialPieces()
                        }
                    }
                }
                QuestMode.Equip -> {
                    if (equipmentPairList == null) {
                        changeEquipInfoPairList()
                    }
                }
            }
            questMode = mode
            appRepository.setQuestMode(mode)
        }
    }

    fun addSearches() {
        var id = 0
        searchSet.forEach { pair ->
            if (pair.first > id) {
                id = pair.first
            }
        }
        id++
        val newSearches = Array(3) { 0 }
        searchSet = searchSet.plus(id to newSearches)
        searches = newSearches
        searchId = id
        visitIndex = 1
        if (equipMaterialPairList == null || memoryPieces == null) {
            changeMaterialPieces()
        }
        appRepository.setQuestSearchId(searchId)
        appRepository.setQuestSearchSet(searchSet)
    }

    fun delSearches(id: Int) {
        searchSet = searchSet.filter { it.first != id }
        if (searchSet.size == 1) {
            searchSet = listOf(1 to searches)
            searchId = 1
            appRepository.setQuestSearchId(1)
            appRepository.setQuestSearchSet(searchSet)
        } else {
            appRepository.setQuestSearchSet(searchSet)
        }
    }

    fun changeSearchId(id: Int) {
        val pair = searchSet.find { it.first == id }
        if (pair != null) {
            searchId = pair.first
            searches = pair.second
            changeSearchQuestDataList()
            appRepository.setQuestSearchId(searchId)
        }
    }

    fun changeSearches(materialId: Int) {
        if (visitIndex == 0) {
            visitIndex = 1
            if (equipMaterialPairList == null || memoryPieces == null) {
                changeMaterialPieces()
            }
        } else {
            if (searches.contains(materialId)) {
                val list = searches.filter { it != materialId }.toMutableList()
                setSearches(searchId, list)
            } else {
                val list = searches.filter { it != 0 }.toMutableList()
                if (list.size < 3) {// TODO 提示最多选3个
                    list.add(materialId)
                    setSearches(searchId, list)
                }
            }
        }
    }

    fun changeHighlightList(materialId: Int) {
        highlightList = if (highlightList.contains(materialId)) {
            highlightList.minus(materialId)
        } else {
            highlightList.plus(materialId)
        }
    }

    fun changeEquipTypes(checked: Boolean, type: Int) {
        equipTypes = if (checked) {
            equipTypes.minus(type)
        } else {
            equipTypes.plus(type)
        }
    }

    fun changeAllEquipTypes(allSelected: Boolean) {
        equipTypes = if (allSelected) {
            emptySet()
        } else {
            getAllEquipTypes()
        }
    }

    fun changeQuestType(type: QuestType) {
        if (type != questType) {
            questType = type
            changeQuestDataList()
            appRepository.setQuestMapType(type)
        }
    }

    fun changeSearchTypes(type: QuestType) {
        searchTypes = if (searchTypes.contains(type)) {
            searchTypes.filter { it != type }.toTypedArray()
        } else {
            searchTypes.plus(type)
        }
        if (allQuestDataList.isNotEmpty()) {
            questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, searchTypes, min37)
        }
    }

    fun toggleMin37() {
        min37 = !min37
        if (allQuestDataList.isNotEmpty()) {
            questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, searchTypes, min37)
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

    fun toggleVisitIndex() {
        visitIndex = if (visitIndex == 1) 2 else 1
    }

    fun changeArea(value: Int) {
        if (value != area) {
            area = value
            changeQuestDataList()
            appRepository.setQuestMapArea(value)
        }
    }

    fun search() {
        if (visitIndex == 0) {
            visitIndex = 1
            if (equipMaterialPairList == null || memoryPieces == null) {
                changeMaterialPieces()
            }
        } else {
            visitIndex = 0
            changeSearchQuestDataList()
            appRepository.setQuestSearchSet(searchSet)
        }
    }

    fun destroy() {
        questDataList = null
        equipmentPairList = null
        equipMaterialPairList = null
        memoryPieces = null
//        dropRangeMap = null
    }

    private fun changeQuestDataList() {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val questRange = when (questType) {
                QuestType.S -> QuestRange.S
                QuestType.VH -> QuestRange.VH
                else -> {
                    val value = (questType.value + area) * 1000
                    QuestRange(value + 1, value + 15)
                }
            }
            val list = db.getQuestDataList(questRange)
            highlightList = emptyList()
            questDataList = if (sortDesc) {
                list.sortedByDescending { it.questId }
            } else {
                list.sortedBy { it.questId }
            }
        }
    }

    private fun changeSearchQuestDataList() {
        searchedList = searches.filter { it != 0 }
        if (searchedList.isEmpty() || searchTypes.isEmpty()) {
            questDataList = emptyList()
        } else {
            questDataList = null
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                allQuestDataList = db.getQuestDataList(searchedList, sortDesc)
                questDataList = QuestRange.getFilteredQuestDataList(allQuestDataList, searchTypes, min37)
            }
        }
    }

    private fun changeEquipInfoPairList() {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            equipmentPairList = db.getEquipmentPairList()
            typePairList = EquipInfo.typePairList
            equipTypes = getAllEquipTypes()
        }
    }

    private fun changeMaterialPieces() {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val materials = async { db.getEquipMaterialPairList() }
            val pieces = async { db.getMemoryPieces() }
            equipMaterialPairList = materials.await()
            memoryPieces = pieces.await()
        }
    }

    private fun getAllEquipTypes(): Set<Int> {
        return if (typePairList != null) {
            val set = mutableSetOf<Int>()
            typePairList!!.forEach { pair ->
                set.add(pair.first)
            }
            set
        } else {
            emptySet()
        }
    }

    private fun setSearches(id: Int, list: MutableList<Int>) {
        var paddingSize = 3 - list.size
        while (paddingSize > 0) {
            paddingSize--
            list.add(0)
        }
        val newSearches = list.toTypedArray()
        if (id == searchId) {
            searches = newSearches
        }
        searchSet = searchSet
            .filter { it.first != id }
            .plus(id to newSearches)
    }
}
