package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.QuestWaveGroupEnemy
import com.kasuminotes.db.getMirageQuestDataList
import com.kasuminotes.db.hasMirageQuest
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MirageQuestState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
//    val mirageQuestList = listOf("mirage_nemesis_quest", "mirage_floor_quest")

    var hasMirageQuest by mutableStateOf(false)
        private set

//    var selectedQuest by mutableIntStateOf(0)
//        private set

    var mirageQuestDataGrouped by mutableStateOf<Map<Int, List<QuestWaveGroupEnemy>>>(emptyMap())
        private set

    var nemesisQuestDataGrouped by mutableStateOf<Map<Int, List<QuestWaveGroupEnemy>>>(emptyMap())
        private set

    fun initMirageQuest() {
        hasMirageQuest = false
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            hasMirageQuest = db.hasMirageQuest()
            if (hasMirageQuest) {
//                selectQuest(0)
                val mirageQuestDataList = db.getMirageQuestDataList("mirage_floor_quest")
                if (mirageQuestDataList.isEmpty()) {
                    hasMirageQuest = false
                } else {
                    mirageQuestDataGrouped = mirageQuestDataList.groupBy { it.questId }
                }
                val nemesisQuestDataList = db.getMirageQuestDataList("mirage_nemesis_quest")
                if (nemesisQuestDataList.isEmpty()) {
                    hasMirageQuest = false
                } else {
                    nemesisQuestDataGrouped = nemesisQuestDataList.groupBy { it.questId }
                }
            }
        }
    }

//    fun selectQuest(index: Int) {
//        selectedQuest = index
//        scope.launch(Dispatchers.IO) {
//            val db = appRepository.getDatabase()
//            val mirageQuestDataList = db.getMirageQuestDataList(mirageQuestList[index])
//            if (mirageQuestDataList.isEmpty()) {
//                hasMirageQuest = false
//            } else {
//                mirageQuestDataGrouped = mirageQuestDataList.groupBy { it.questId }
//            }
//        }
//    }
}
