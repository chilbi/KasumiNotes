package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.DungeonAreaData
import com.kasuminotes.db.getDungeonAreaDataList
import com.kasuminotes.db.getEnemyTalentWeaknessMap
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DungeonState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var hasDungeon by mutableStateOf(false)
        private set

    var dungeonAreaDataGrouped by mutableStateOf<Map<Int, List<DungeonAreaData>>>(emptyMap())
        private set

    var enemyTalentWeaknessMap by mutableStateOf<Map<Int, List<Int>>>(emptyMap())
        private set

    fun initAreaDataList() {
        hasDungeon = false
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val dungeonAreaDataList = db.getDungeonAreaDataList()
            hasDungeon = dungeonAreaDataList.isNotEmpty()
            if (hasDungeon) {
                val enemyIdList = dungeonAreaDataList.map { it.enemyId }
                enemyTalentWeaknessMap = db.getEnemyTalentWeaknessMap(enemyIdList)
                dungeonAreaDataGrouped = dungeonAreaDataList.groupBy { it.dungeonAreaId }
            }
        }
    }
}
