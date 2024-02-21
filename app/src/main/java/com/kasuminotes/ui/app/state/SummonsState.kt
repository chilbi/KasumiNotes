package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.SummonData
import com.kasuminotes.data.UserData
import com.kasuminotes.db.getMultiEnemyParts
import com.kasuminotes.db.getSummonData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SummonsState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var summonDataList by mutableStateOf<List<SummonData>>(emptyList())
        private set

    var minionDataList by mutableStateOf<List<EnemyData>>(emptyList())
        private set

    fun initSummons(summons: List<Int>, skillLevel: Int, userData: UserData) {
        destroy()
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            summonDataList = summons.map { unitId ->
                async {
                    val summonData = db.getSummonData(unitId)
                    summonData.load(db, skillLevel, userData)
                    summonData
                }
            }.awaitAll()
        }
    }

    fun initMinionDataList(minions: List<Int>) {
        destroy()
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val enemyDataList = db.getMultiEnemyParts(minions)
            minionDataList = enemyDataList.map { enemyData ->
                async {
                    enemyData.load(db)
                    enemyData
                }
            }.awaitAll()
        }
    }

    fun destroy() {
        summonDataList = emptyList()
        minionDataList = emptyList()
    }
}
