package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.AbyssSchedule
import com.kasuminotes.data.QuestWaveGroupEnemy
import com.kasuminotes.db.getAbyssQuestDataList
import com.kasuminotes.db.getAbyssScheduleList
import com.kasuminotes.db.hasAbyssQuest
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AbyssQuestState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var hasAbyssQuest by mutableStateOf(false)
        private set

    var abyssScheduleList by mutableStateOf<List<AbyssSchedule>>(emptyList())
        private set

    var selectedSchedule by mutableStateOf<AbyssSchedule?>(null)
        private set

    var abyssQuestDataGrouped by mutableStateOf<Map<Int, List<QuestWaveGroupEnemy>>>(emptyMap())
        private set

    fun initAbyssScheduleList() {
        hasAbyssQuest = false
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            hasAbyssQuest = db.hasAbyssQuest()
            if (hasAbyssQuest) {
                abyssScheduleList = db.getAbyssScheduleList()
                if (abyssScheduleList.isEmpty()) {
                    hasAbyssQuest = false
                } else {
                    selectSchedule(abyssScheduleList[0])
                }
            }
        }
    }

    fun selectSchedule(schedule: AbyssSchedule) {
        selectedSchedule = schedule
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val abyssQuestDataList = db.getAbyssQuestDataList(schedule.abyssId)
            if (abyssQuestDataList.isEmpty()) {
                hasAbyssQuest = false
            } else {
                abyssQuestDataGrouped = abyssQuestDataList.groupBy { it.questId }
            }
        }
    }
}
