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
    private var onExEquipDataChange: ((slotNum: Int, exEquip: ExEquipData?) -> Unit)? = null
    private var onEnhanceLevelChange: ((slotNum: Int, level: Int) -> Unit)? = null
    private var slotNum = 0

    var exEquipSlot by mutableStateOf<ExEquipSlot?>(null)
        private set
    var exEquipData by mutableStateOf<ExEquipData?>(null)
        private set
    var exEquipCategory by mutableStateOf<ExEquipCategory?>(null)
        private set
    var equippableExList by mutableStateOf<List<Int>>(emptyList())
        private set
    var percentProperty by mutableStateOf(Property.zero)
        private set
    var baseProperty by mutableStateOf(Property.zero)
        private set
    var isEquipping by mutableStateOf(false)
        private set
    var maxEnhanceLevel by mutableStateOf(0)
        private set
    var enhanceLevel by mutableStateOf(0)
        private set
    var originEnhanceLevel by mutableStateOf(0)
        private set

    suspend fun selectExEquipSlot(
        slot: ExEquipSlot,
        charaBaseProperty: Property,
        originLevel: Int = 0,
        onExEquipChange: ((slotNum: Int, exEquip: ExEquipData?) -> Unit)? = null,
        onLevelChange: ((slotNum: Int, level: Int) -> Unit)? = null
    ) = withContext(defaultDispatcher) {
        exEquipSlot = slot
        baseProperty = charaBaseProperty
        enhanceLevel = originLevel
        originEnhanceLevel = originLevel
        isEquipping = false
        onExEquipDataChange = onExEquipChange
        onEnhanceLevelChange = onLevelChange
        slotNum = slot.category / 100

        val db = appRepository.getDatabase()
        val list = awaitAll(
            async { db.getExEquipCategory(slot.category) },
            async { db.getEquippableExList(slot.category) }
        )
        exEquipCategory = list[0] as ExEquipCategory
        @Suppress("UNCHECKED_CAST")
        equippableExList = list[1] as List<Int>
        if (slot.exEquipData != null) {
            exEquipData = slot.exEquipData
            percentProperty = slot.exEquipData.getPercentProperty(originEnhanceLevel)
            maxEnhanceLevel = slot.exEquipData.maxEnhanceLevel
            isEquipping = true
        }
    }

    fun selectExEquip(exEquipId: Int) {
        scope.launch(defaultDispatcher) {
            val db = appRepository.getDatabase()
            val exEquip = db.getExEquipData(exEquipId)
            exEquipData = exEquip
            percentProperty = exEquip.getPercentProperty(exEquip.maxEnhanceLevel)
            maxEnhanceLevel = exEquip.maxEnhanceLevel
            if (exEquipSlot!!.exEquipData != null) {
                isEquipping = exEquip.exEquipmentId == exEquipSlot!!.exEquipData!!.exEquipmentId
            }
            enhanceLevel = if (isEquipping) originEnhanceLevel else exEquip.maxEnhanceLevel
        }
    }

    fun changeEnhanceLevel(level: Int) {
        percentProperty = exEquipData!!.getPercentProperty(level)
        enhanceLevel = level
        if (onEnhanceLevelChange != null && isEquipping) {
            onEnhanceLevelChange!!(slotNum, level)
        }
    }

    fun changeExEquip() {
        if (onExEquipDataChange != null && onEnhanceLevelChange != null) {
            if (isEquipping) {
                isEquipping = false
                exEquipSlot = exEquipSlot!!.copy(exEquipData = null)
                onExEquipDataChange!!(slotNum, null)
                onEnhanceLevelChange!!(slotNum, -1)
            } else {
                isEquipping = true
                exEquipSlot = exEquipSlot!!.copy(exEquipData = exEquipData)
                originEnhanceLevel = enhanceLevel
                onExEquipDataChange!!(slotNum, exEquipData)
                onEnhanceLevelChange!!(slotNum, enhanceLevel)
            }
        }
    }

    fun destroy() {
        exEquipSlot = null
        exEquipData = null
        exEquipCategory = null
        equippableExList = emptyList()
        percentProperty = Property.zero
        baseProperty = Property.zero
        isEquipping = false
        maxEnhanceLevel = 0
        enhanceLevel = 0
        maxEnhanceLevel = 0
        slotNum = 0
        onExEquipDataChange = null
        onEnhanceLevelChange = null
    }
}
