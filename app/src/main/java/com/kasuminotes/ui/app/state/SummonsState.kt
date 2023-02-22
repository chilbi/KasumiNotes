package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.SummonData
import com.kasuminotes.data.UserData
import com.kasuminotes.db.getSummonData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SummonsState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    var summonDataList by mutableStateOf<List<SummonData>>(emptyList())
        private set

    fun initSummons(summons: List<Int>, skillLevel: Int, userData: UserData) {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            val deferredList = summons.map { unitId ->
                async {
                    val summonData = db.getSummonData(unitId)
                    summonData.load(db, skillLevel, userData, defaultDispatcher)
                    summonData
                }
            }
            summonDataList = deferredList.awaitAll()
        }
    }

    fun destroy() {
        summonDataList = emptyList()
    }
}
