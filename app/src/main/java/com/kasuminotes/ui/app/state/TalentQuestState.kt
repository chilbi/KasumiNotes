package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.TalentQuestAreaData
import com.kasuminotes.data.TalentQuestData
import com.kasuminotes.db.getTalentQuestAreaDataList
import com.kasuminotes.db.getTalentQuestDataList
import com.kasuminotes.db.hasTalentQuest
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TalentQuestState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    private var talentQuestDataList: List<TalentQuestData> = emptyList()

    var hasTalentQuest by mutableStateOf(false)
        private set

    var talentQuestAreaDataList by mutableStateOf<List<TalentQuestAreaData>>(emptyList())
        private set

    var selectedArea by mutableStateOf<TalentQuestAreaData?>(null)
        private set

    var selectedNum by mutableStateOf(1)
        private set

    var maxNum by mutableStateOf(1)
        private set

    var talentQuestDataGrouped by mutableStateOf<Map<Int, List<TalentQuestData>>>(emptyMap())
        private set

    fun initTalentQuestDataList() {
        hasTalentQuest = false
        selectedNum = 1
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            hasTalentQuest = db.hasTalentQuest()
            if (hasTalentQuest) {
                talentQuestAreaDataList = db.getTalentQuestAreaDataList()
                selectArea(talentQuestAreaDataList[0])
            }
        }
    }

    fun selectArea(areaData: TalentQuestAreaData) {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            talentQuestDataList = db.getTalentQuestDataList(areaData.areaId)
            maxNum = (talentQuestDataList.maxOf { it.questId } % 1000) / 10
            selectedArea = areaData
            selectedNum = maxNum
            selectTalentQuestDataList()
        }
    }

    fun selectNum(num: Int) {
        selectedNum = num
        selectTalentQuestDataList()
    }

    private fun selectTalentQuestDataList() {
        val min = selectedArea!!.areaId * 1000 + (selectedNum - 1) * 10
        val max = min + 10
        val filtered = talentQuestDataList.filter { it.questId in (min + 1)..max }
        talentQuestDataGrouped = filtered.groupBy { it.questId }
    }
}
