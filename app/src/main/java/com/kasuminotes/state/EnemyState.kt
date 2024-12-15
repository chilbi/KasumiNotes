package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.EnemyData
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import com.kasuminotes.db.getEnemyData
import com.kasuminotes.db.getMultiEnemyParts
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EnemyState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    var epTableName by mutableStateOf("")
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

    fun initEnemy(enemy: EnemyData, weaknessList: List<Int>, epName: String, waveGroupId: Int?) {
        enemy.waveGroupId = waveGroupId
        epTableName = epName
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
                enemy.enemyMultiParts = db.getMultiEnemyParts(enemy.multiParts, epName)
                enemy.load(db)
                enemyMultiParts = enemy.enemyMultiParts
                unitAttackPatternList = enemy.unitAttackPatternList
                skillList = enemy.skillList
                unitSkillData = enemy.unitSkillData
            }
        }
    }

    fun initEnemy(enemyId: Int, weaknessList: List<Int>, epName: String, waveGroupId: Int?): Boolean {
        val db = appRepository.getDatabase()
        val enemy = db.getEnemyData(enemyId, epName)
        if (enemy != null) {
            initEnemy(enemy, weaknessList, epName, waveGroupId)
            return true
        }
        return false
    }
}
