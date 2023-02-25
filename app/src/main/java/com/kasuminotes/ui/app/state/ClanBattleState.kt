package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.db.getClanBattlePeriodList
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClanBattleState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    var clanBattlePeriodList by mutableStateOf<List<ClanBattlePeriod>>(emptyList())
        private set

    fun initPeriodList() {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            clanBattlePeriodList = db.getClanBattlePeriodList()
        }
    }
}
