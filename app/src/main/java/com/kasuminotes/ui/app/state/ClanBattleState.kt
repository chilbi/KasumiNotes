package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.ClanBattlePeriod
import com.kasuminotes.data.DungeonAreaData
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.db.getClanBattleMapDataList
import com.kasuminotes.db.getClanBattlePeriodList
import com.kasuminotes.db.getEnemyData
import com.kasuminotes.db.getMultiEnemyParts
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

    var talentWeaknessList: List<Int> by mutableStateOf(emptyList())
        private set

    var enemyData by mutableStateOf<EnemyData?>(null)
        private set

    var enemyMultiParts by mutableStateOf<List<EnemyData>>(emptyList())
        private set

    var unitAttackPatternList by mutableStateOf<List<UnitAttackPattern>>(emptyList())
        private set

    var skillList by mutableStateOf<List<SkillItem>>(emptyList())
        private set

    var unitSkillData by mutableStateOf<UnitSkillData?>(null)
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

    fun initEnemy(enemy: EnemyData, weaknessList: List<Int>) {
        enemyData = enemy
        talentWeaknessList = weaknessList
        enemyMultiParts = enemy.enemyMultiParts
        unitAttackPatternList = enemy.unitAttackPatternList
        skillList = enemy.skillList
        unitSkillData = enemy.unitSkillData
        if (
            (enemy.multiParts.isNotEmpty() && enemy.enemyMultiParts.isEmpty()) ||
            enemy.unitSkillData == null ||
            enemy.unitAttackPatternList.isEmpty()
        ) {
            scope.launch(Dispatchers.IO) {
                val db = appRepository.getDatabase()
                enemy.enemyMultiParts = db.getMultiEnemyParts(enemy.multiParts)
                enemy.load(db)
                enemyMultiParts = enemy.enemyMultiParts
                unitAttackPatternList = enemy.unitAttackPatternList
                skillList = enemy.skillList
                unitSkillData = enemy.unitSkillData
            }
        }
    }

fun initEnemy(enemyId: Int, weaknessList: List<Int>): Boolean {
        val db = appRepository.getDatabase()
        val enemy = db.getEnemyData(enemyId)
        if (enemy != null) {
            initEnemy(enemy, weaknessList)
            return true
        }
        return false
    }

    fun destroy() {
        enemyMultiParts = emptyList()
        unitAttackPatternList = emptyList()
        skillList = emptyList()
        unitSkillData = null
    }
}
