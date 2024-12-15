package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.db.getClanBattleMapDataList
import com.kasuminotes.db.getClanBattlePeriodList
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClanBattleState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var clanBattlePeriodList by mutableStateOf<List<ClanBattlePeriod>>(emptyList())
        private set

    var clanBattlePeriod by mutableStateOf<ClanBattlePeriod?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var isAll by mutableStateOf(false)
        private set

    var loading by mutableStateOf(false)
        private set

    fun loadAll() {
        scope.launch(Dispatchers.IO) {
            loading = true
            val db = appRepository.getDatabase()
            clanBattlePeriodList = db.getClanBattlePeriodList(false)
            isAll = true
            loading = false
        }
    }

    fun initPeriodList() {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            clanBattlePeriodList = db.getClanBattlePeriodList(true)
            isAll = false
        }
    }

    fun initPeriod(label: String, period: ClanBattlePeriod) {
        if (period.mapDataList.isEmpty()) {
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                var list = db.getClanBattleMapDataList(period.clanBattleId)

                list = if (period.periodNum > 10) {
                    list.filter { it.lapNumTo != 1 }
                } else if (period.periodNum > 8){
                    listOf(list[0].copy(phase = 3), list[1].copy(phase = 2), list[2])
                } else {
                    listOf(list[0].copy(phase = 2), list[1])
                }

                period.mapDataList = list
                clanBattlePeriod = period
                title = label
            }
        } else {
            clanBattlePeriod = period
            title = label
        }
    }
}
