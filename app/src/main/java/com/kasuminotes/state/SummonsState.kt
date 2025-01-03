package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.ExtraEffectData
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
    var extraEffectData by mutableStateOf<ExtraEffectData?>(null)
        private set

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

    fun initMinionDataList(minions: List<Int>, epTableName: String, extraEffect: ExtraEffectData?) {
        destroy()
        extraEffectData = extraEffect
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val enemyDataList = db.getMultiEnemyParts(minions, epTableName)
            minionDataList = enemyDataList.map { enemyData ->
                async {
                    enemyData.load(db, epTableName)
                    enemyData
                }
            }.awaitAll()
        }
    }

    private fun destroy() {
        extraEffectData = null
        summonDataList = emptyList()
        minionDataList = emptyList()
    }
}
