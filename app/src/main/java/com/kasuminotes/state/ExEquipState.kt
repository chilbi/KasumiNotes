package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.action.toNumStr
import com.kasuminotes.data.ExEquipCategory
import com.kasuminotes.data.ExEquipData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.Property
import com.kasuminotes.db.getEquippableExList
import com.kasuminotes.db.getExEquipCategory
import com.kasuminotes.db.getExEquipData
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ExEquipState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    private var onExEquipDataChange: ((slotNum: Int, exEquip: ExEquipData?) -> Unit)? = null
    private var onEnhanceLevelChange: ((slotNum: Int, level: Int) -> Unit)? = null
    private var onSubPercentListChange: ((slotNum: Int, subPercentList: List<Pair<Int, Double>>) -> Unit)? = null
    private var slotNum = 0
    private var exSkillProperty = Property.zero
    private var originSubPercentList: List<Pair<Int, Double>> = emptyList()
    private var originEnhanceLevel = 0

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
    var subPercentList by mutableStateOf<List<Pair<Int, Double>>>(emptyList())
        private set
    var baseProperty by mutableStateOf(Property.zero)
        private set
    var battleProperty by mutableStateOf(Property.zero)
        private set
    var isEquipping by mutableStateOf(false)
        private set
    var maxEnhanceLevel by mutableIntStateOf(0)
        private set
    var enhanceLevel by mutableIntStateOf(0)
        private set

    fun initExEquipSlot(
        slot: ExEquipSlot,
        charaBaseProperty: Property,
        charaExSkillProperty: Property,
        originSubStatusPercentList: List<Pair<Int, Double>> = emptyList(),
        originLevel: Int = 0,
        onExEquipChange: ((slotNum: Int, exEquip: ExEquipData?) -> Unit)? = null,
        onLevelChange: ((slotNum: Int, level: Int) -> Unit)? = null,
        onSubChange: ((slotNum: Int, subPercentList: List<Pair<Int, Double>>) -> Unit)? = null
    ) {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val list = awaitAll(
                async { db.getExEquipCategory(slot.category) },
                async { db.getEquippableExList(slot.category) }
            )

            exEquipSlot = slot
            baseProperty = charaBaseProperty
            exSkillProperty = charaExSkillProperty
            originSubPercentList = originSubStatusPercentList
            subPercentList = originSubStatusPercentList
            enhanceLevel = originLevel
            originEnhanceLevel = originLevel
            isEquipping = false
            onExEquipDataChange = onExEquipChange
            onEnhanceLevelChange = onLevelChange
            onSubPercentListChange = onSubChange
            slotNum = slot.category / 100
            exEquipCategory = list[0] as ExEquipCategory
            @Suppress("UNCHECKED_CAST")
            equippableExList = list[1] as List<Int>
            if (slot.exEquipData != null) {
                exEquipData = slot.exEquipData
                percentProperty = slot.exEquipData.getPercentProperty(originEnhanceLevel)
                maxEnhanceLevel = slot.exEquipData.maxEnhanceLevel
                isEquipping = true
                totalBattleProperty()
            }
        }
    }

    fun initExEquip(exEquipId: Int) {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val exEquip = db.getExEquipData(exEquipId)
            exEquipData = exEquip
            percentProperty = exEquip.getPercentProperty(exEquip.maxEnhanceLevel)
            maxEnhanceLevel = exEquip.maxEnhanceLevel
            if (exEquipSlot!!.exEquipData != null) {
                isEquipping = exEquip.exEquipmentId == exEquipSlot!!.exEquipData!!.exEquipmentId
            }
            enhanceLevel = if (isEquipping) originEnhanceLevel else exEquip.maxEnhanceLevel
            subPercentList = if (exEquip.subStatusList == null) {
                emptyList()
            } else {
                if (isEquipping) originSubPercentList else exEquip.generateSubPercentList()
            }
            totalBattleProperty()
        }
    }

    fun changeSubPercentList() {
        subPercentList = exEquipData!!.generateSubPercentList()
        if (onSubPercentListChange != null && isEquipping) {
            onSubPercentListChange!!(slotNum, subPercentList)
        }
    }

    fun changeSubPercent(index: Int, status: Int) {
        val list = subPercentList.toMutableList()
        val subStatus = exEquipData!!.subStatusList!!.find { it.status == status }!!
        list[index] = status to exEquipData!!.generateValue(subStatus)
        subPercentList = list
        if (onSubPercentListChange != null && isEquipping) {
            onSubPercentListChange!!(slotNum, list)
        }
    }

    fun changeSubPercentValue(index: Int, value: Double) {
        val list = subPercentList.toMutableList()
        val subPercent = list[index]
        list[index] = subPercent.first to value
        subPercentList = list
        if (onSubPercentListChange != null && isEquipping) {
            onSubPercentListChange!!(slotNum, list)
        }
    }

    fun changeEnhanceLevel(level: Int) {
        percentProperty = exEquipData!!.getPercentProperty(level)
        enhanceLevel = level
        if (onEnhanceLevelChange != null && isEquipping) {
            onEnhanceLevelChange!!(slotNum, level)
        }
        totalBattleProperty()
    }

    fun changeExEquip() {
        if (onExEquipDataChange != null && onEnhanceLevelChange != null && onSubPercentListChange != null) {
            if (isEquipping) {
                isEquipping = false
                exEquipSlot = exEquipSlot!!.copy(exEquipData = null)
                onExEquipDataChange!!(slotNum, null)
                onEnhanceLevelChange!!(slotNum, -1)
                onSubPercentListChange!!(slotNum, emptyList())
            } else {
                isEquipping = true
                exEquipSlot = exEquipSlot!!.copy(exEquipData = exEquipData)
                originEnhanceLevel = enhanceLevel
                onExEquipDataChange!!(slotNum, exEquipData)
                onEnhanceLevelChange!!(slotNum, enhanceLevel)
                onSubPercentListChange!!(slotNum, subPercentList)
            }
        }
    }

    fun valueDisplay(index: Int, value: Double): String {
        return if (index < 7) {
            "${(value / 100).toNumStr()}%(+${(baseProperty[index] * value / 10000).roundToInt()})"// TODO 不确定的取整方式
        } else {
            value.roundToInt().toString()// TODO 不确定的取整方式
        }
    }

    fun destroy() {
        exEquipSlot = null
        exEquipData = null
        exEquipCategory = null
        equippableExList = emptyList()
        subPercentList = emptyList()
        percentProperty = Property.zero
        baseProperty = Property.zero
        battleProperty = Property.zero
        isEquipping = false
        maxEnhanceLevel = 0
        enhanceLevel = 0
        maxEnhanceLevel = 0
        slotNum = 0
        onExEquipDataChange = null
        onEnhanceLevelChange = null
    }

    private fun totalBattleProperty() {
        val equipProperty = exEquipData!!.getProperty(
            subPercentList,
            percentProperty,
            baseProperty,
            exSkillProperty,
            false
        )
        battleProperty = Property { index ->
            baseProperty[index] + exSkillProperty[index] + equipProperty[index]
        }
    }
}
