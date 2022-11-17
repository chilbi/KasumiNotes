package com.kasuminotes.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.ExEquipCategory
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.Property
import com.kasuminotes.db.getEquippableExList
import com.kasuminotes.db.getExEquipCategory
import com.kasuminotes.db.getExEquipData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExEquipState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private var exEquipSlotRef: ExEquipSlot? = null
    var exEquipCategory by mutableStateOf<ExEquipCategory?>(null)
        private set
    var equippableExList by mutableStateOf<List<Int>>(emptyList())
        private set
    var exEquipData by mutableStateOf<ExEquipData?>(null)
        private set
    var newExEquipData by mutableStateOf<ExEquipData?>(null)
        private set
    var percentProperty by mutableStateOf(Property.zero)
        private set
    var baseProperty by mutableStateOf(Property.zero)
        private set

    suspend fun selectExEquipSlot(exEquipSlot: ExEquipSlot, charaBaseProperty: Property) = withContext(defaultDispatcher) {
        exEquipSlotRef = exEquipSlot
        baseProperty = charaBaseProperty
        val db = appRepository.getDatabase()
        val list = awaitAll(
            async {
                exEquipSlot.exEquipCategory ?: db.getExEquipCategory(exEquipSlot.category)
            },
            async {
                exEquipSlot.equippableExList.ifEmpty { db.getEquippableExList(exEquipSlot.category) }
            }
        )
        exEquipCategory = list[0] as ExEquipCategory
        @Suppress("UNCHECKED_CAST")
        equippableExList = list[1] as List<Int>
        exEquipData = exEquipSlot.exEquipData
    }

    fun selectExEquip(exEquipId: Int) {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            val exEquip = db.getExEquipData(exEquipId)
            exEquipData = exEquip
            percentProperty = exEquip.getPercentProperty(exEquip.maxEnhanceLevel)
        }
    }
}
