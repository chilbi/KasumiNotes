package com.kasuminotes.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasuminotes.data.ExperienceTalentLevel
import com.kasuminotes.data.RoleMasteryData
import com.kasuminotes.data.SkillNode
import com.kasuminotes.data.TalentSkillNode
import com.kasuminotes.data.TeamSkillNode
import com.kasuminotes.db.getTalentLevelPair
import com.kasuminotes.db.getMaxTalentLevel
import com.kasuminotes.db.getRoleMasteryDataMap
import com.kasuminotes.db.getTalentSkillNodeList
import com.kasuminotes.db.getTeamSkillNodeList
import com.kasuminotes.ui.app.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class EnhanceState(
    private val appRepository: AppRepository,
    private val scope: CoroutineScope
) {
    private lateinit var talentSkillNodeList: List<TalentSkillNode>
    private lateinit var pagedTalentSkillNode: Map<Int, List<TalentSkillNode>>

    var groupedTalentEnhancedMap by mutableStateOf<Map<Int/*talentId*/, List<Pair<Int/*parameterType*/, Int>>>>(emptyMap())
        private set
    var talentConsumeMap by mutableStateOf<Map<Int/*itemId*/, Int>>(emptyMap())
    var maxTalentLevel by mutableStateOf<Int?>(null)
        private set
    var talentLevelMap by mutableStateOf<Map<Int/*talentId*/, Pair<ExperienceTalentLevel, ExperienceTalentLevel>>?>(null)//talentId
        private set
    var talentLevelEnhancedMap by mutableStateOf<Map<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>>(emptyMap())
        private set
    var groupedTalentLevelEnhancedMap by mutableStateOf<Map<Int/*talentId*/, List<Pair<Int/*parameterType*/, Int>>>>(emptyMap())
        private set
    var talentLevelConsumeMap by mutableStateOf<Map<Int/*itemId*/, Int>>(emptyMap())
        private set
//############################################################################
    var enhancedTalentSkillNodeList by mutableStateOf<List<Pair<TalentSkillNode, Int>>>(emptyList())//node to enhanceLevel
        private set
    var maxPageNum by mutableIntStateOf(0)
        private set
    var currentPageNum by mutableIntStateOf(0)
        private set
    var currentPageNodeList by mutableStateOf<List<TalentSkillNode>?>(null)
        private set
    var connectionList by mutableStateOf<List<ConnectionNode>?>(null)//所有属性节点的连接
        private set
    var enhanceNodeList by mutableStateOf<List<ConnectionNode>?>(null)//去掉重复节点的连接
        private set
    var talentSkillEnhancedMap by mutableStateOf<Map<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>>(emptyMap())
        private set
    var groupedTalentSkillEnhancedMap by mutableStateOf<Map<Int/*talentId*/, List<Pair<Int/*parameterType*/, Int>>>>(emptyMap())
        private set
    var talentSkillConsumeMap by mutableStateOf<Map<Int/*itemId*/, Int>>(emptyMap())
//############################################################################
    var teamSkillNodeList by mutableStateOf<List<TeamSkillNode>?>(null)
        private set
    var enhancedTeamSkillNode by mutableStateOf<TeamSkillNode?>(null)
        private set
    var teamSkillEnhancedMap by mutableStateOf<Map<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>>(emptyMap())
        private set
    var groupedTeamSkillEnhancedMap by mutableStateOf<Map<Int/*talentId*/, List<Pair<Int/*parameterType*/, Int>>>>(emptyMap())
        private set
    var teamSkillConsumeMap by mutableStateOf<Map<Int/*itemId*/, Int>>(emptyMap())
//############################################################################
    var roleMasteryDataMap by mutableStateOf<Map<Int, RoleMasteryData>?>(null)
        private set
    var roleEnhanceMap by mutableStateOf<Map<Int/*roleId*/, Map<Int/*slotId*/, Pair<Int/*slotLevel*/, Int/*enhanceLevel*/>>>?>(null)
        private set
    var roleIdList by mutableStateOf<List<Int>>(emptyList())
        private set
    var selectedRoleId by mutableIntStateOf(0)
        private set
    var selectedSlotId by mutableIntStateOf(0)
        private set
//    var roleEnhancedMap by mutableStateOf<Map<Pair<Int/*roleId*/, Int/*parameterType*/>, Int>>(emptyMap())
//        private set
    var groupedRoleEnhancedMap by mutableStateOf<Map<Int/*roleId*/, List<Pair<Int/*parameterType*/, Int>>>>(emptyMap())
        private set
//    var roleConsumeMap by mutableStateOf<Map<Int/*itemId*/, Int>>(emptyMap())
//        private set
    var roleConsumeMapEntries by mutableStateOf<List<Map.Entry<Int, Int>>>(emptyList())
        private set

    fun initState() {
        talentLevelMap = null
        currentPageNodeList = null
        teamSkillNodeList = null
        roleMasteryDataMap = null

        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            awaitAll(
                async {
                    maxTalentLevel = db.getMaxTalentLevel()
                    if (maxTalentLevel != null) {
                        val talentLevelPair = db.getTalentLevelPair(maxTalentLevel!!, maxTalentLevel!!)
                        if (talentLevelPair != null) {
                            val map = mutableMapOf<Int, Pair<ExperienceTalentLevel, ExperienceTalentLevel>>()
                            (1..5).forEach { talentId ->
                                map[talentId] = talentLevelPair
                            }
                            talentLevelMap = map
                            totalTalentLevelEnhance()
                        }
                    }
                },
                async {
                    val nodeList = db.getTalentSkillNodeList()
                    if (!nodeList.isNullOrEmpty()) {
                        talentSkillNodeList = nodeList
                        val grouped = nodeList.groupBy { it.pageNum }
                        val maxPage = grouped.entries.maxOf { it.key }
                        val curPageNodeList = grouped[maxPage]
                        val finishNode = nodeList[0]
                        val enhancedNodeList = listOf(finishNode to finishNode.maxLevel)
                        changeConnectionNode(curPageNodeList!!, enhancedNodeList)
                        pagedTalentSkillNode = grouped
                        enhancedTalentSkillNodeList = enhancedNodeList
                        maxPageNum = maxPage
                        currentPageNum = maxPage
                        currentPageNodeList = curPageNodeList
                        totalTalentSkillEnhance()
                    }
                },
                async {
                    val nodeList = db.getTeamSkillNodeList()
                    if (!nodeList.isNullOrEmpty()) {
                        teamSkillNodeList = nodeList
                        enhancedTeamSkillNode = nodeList[0]
                        totalTeamSkillEnhance()
                    }
                },
                async {
                    val roleMap = db.getRoleMasteryDataMap()
                    if (!roleMap.isNullOrEmpty()) {
                        val enhanceMap = mutableMapOf<Int/*roleId*/, Map<Int/*slotId*/, Pair<Int/*slotLevel*/, Int/*enhanceLevel*/>>>()
                        roleMap.forEach { (roleId, roleMasteryData) ->
                            val slotIdEnhanceMap = mutableMapOf<Int, Pair<Int, Int>>()
                            roleMasteryData.slotIdMap.forEach { (slotId, roleSlotData) ->
                                val slotLevel = roleSlotData.maxSlotLevel
                                val roleSlotLevel = roleSlotData.slotLevelMap[slotLevel]!!
                                val enhanceLevel = roleSlotLevel.maxEnhanceLevel
                                slotIdEnhanceMap[slotId] = slotLevel to enhanceLevel
                            }
                            enhanceMap[roleId] = slotIdEnhanceMap
                        }
                        roleIdList = roleMap.keys.toList()
                        selectedRoleId = roleIdList[0]
                        selectedSlotId = roleMap[selectedRoleId]!!.slotIdMap.keys.first()
                        roleMasteryDataMap = roleMap
                        roleEnhanceMap = enhanceMap
                        totalRoleEnhance()
                    }
                }
            )
            totalTalentEnhance()
        }
    }

    fun changeTalentLevel(talentId: Int, talentLevel: Int, toTalentLevel: Int) {
        scope.launch(Dispatchers.IO) {
            val db = appRepository.getDatabase()
            val talentLevelPair = db.getTalentLevelPair(talentLevel, toTalentLevel)
            if (talentLevelPair != null) {
                val newTalentLevelMap = talentLevelMap!!.toMutableMap()
                newTalentLevelMap[talentId] = talentLevelPair
                talentLevelMap = newTalentLevelMap
                totalTalentLevelEnhance()
                totalTalentEnhance()
            }
        }
    }

    fun enhanceTalentNode(node: TalentSkillNode, level: Int) {
        val newEnhancedList = mutableListOf<Pair<TalentSkillNode, Int>>()
        newEnhancedList.add(node to level)
        if (enhancedTalentSkillNodeList.isNotEmpty()) {
            val isConfluenceNode = node.preNodeList.size > 1 || node.nodeId == 1
            if (!isConfluenceNode) {
                val childrenConfluenceNode = getChildrenConfluenceNode(node)
                var validEnhancedList = enhancedTalentSkillNodeList.filter { it.first.nodeId > childrenConfluenceNode.nodeId }
                if (validEnhancedList.isNotEmpty()) {
                    val parentConfluenceNode = getParentConfluenceNode(node)
                    validEnhancedList = validEnhancedList.filter { it.first.nodeId < parentConfluenceNode.nodeId }
                    if (validEnhancedList.isNotEmpty()) {
                        val invalidNodeList = getInvalidNodeList(parentConfluenceNode, node)
                        validEnhancedList = validEnhancedList.filter {
                            invalidNodeList.find { invalidNode -> invalidNode.nodeId == it.first.nodeId } == null
                        }
                    }
                }
                validEnhancedList.forEach { validEnhanced ->
                    newEnhancedList.add(validEnhanced)
                }
            }
        }
        changeConnectionNode(currentPageNodeList!!, newEnhancedList)
        enhancedTalentSkillNodeList = newEnhancedList
        totalTalentSkillEnhance()
        totalTalentEnhance()
    }

    fun prevPage() {
        changeCurrentPageNum(currentPageNum - 1)
    }

    fun nextPage() {
        changeCurrentPageNum(currentPageNum + 1)
    }

    fun enhanceTeamNode(node: TeamSkillNode?) {
        if (node == null) {
            enhancedTeamSkillNode = null
        } else {
            val shouldCancel = enhancedTeamSkillNode != null && enhancedTeamSkillNode!!.nodeId == 1 && node.nodeId == 1
            enhancedTeamSkillNode = if (shouldCancel) null else node
        }
        totalTeamSkillEnhance()
        totalTalentEnhance()
    }

    fun selectRoleId(roleId: Int) {
        if (selectedRoleId != roleId) {
            selectedRoleId = roleId
            selectedSlotId = roleMasteryDataMap!![roleId]!!.slotIdMap.keys.first()
        }
    }

    fun selectSlotId(slotId: Int) {
        selectedSlotId = slotId
    }

    fun enhanceRole(slotLevel: Int, enhanceLevel: Int) {
        val newRoleEnhanceMap = roleEnhanceMap!!.toMutableMap()
        val newSlotIdMap = newRoleEnhanceMap[selectedRoleId]!!.toMutableMap()
        newSlotIdMap[selectedSlotId] = slotLevel to enhanceLevel
        newRoleEnhanceMap[selectedRoleId] = newSlotIdMap
        roleEnhanceMap = newRoleEnhanceMap.toMap()
        totalRoleEnhance()
    }

    private fun changeCurrentPageNum(value: Int) {
        val newNodeList = pagedTalentSkillNode[value]
        if (newNodeList != null) {
            changeConnectionNode(newNodeList, enhancedTalentSkillNodeList)
            currentPageNum = value
            currentPageNodeList = newNodeList
        }
    }

    private fun changeConnectionNode(
        pageNodeList: List<TalentSkillNode>,
        enhancedNodeList: List<Pair<TalentSkillNode, Int>>
    ) {
        val parentNode = pageNodeList[0]
        val connections = mutableListOf<ConnectionNode>()
        forEachConnection(parentNode, enhancedNodeList) { connectionNode ->
            connections.add(connectionNode)
        }
        val enhancedNodes = connections.distinctBy { it.node.nodeId }
        connectionList = connections
        enhanceNodeList = enhancedNodes
    }

    private fun forEachConnection(
//        curPageNum: Int?,
        parentNode: TalentSkillNode,
        enhancedNodeList: List<Pair<TalentSkillNode, Int>>,
        action: (ConnectionNode) -> Unit
    ) {
        val currentConfluenceNode = if (enhancedNodeList.isEmpty()) null
        else getChildrenConfluenceNode(enhancedNodeList[0].first)
        val confluenceNodeList = mutableListOf<Int>()
        nodeRecursive(
//            curPageNum,
            parentNode,
            currentConfluenceNode,
            enhancedNodeList,
            confluenceNodeList,
            action
        )
    }

    private fun nodeRecursive(
//        curPageNum: Int?,
        parentNode: TalentSkillNode,
        currentConfluenceNode: TalentSkillNode?,
        enhancedNodeList: List<Pair<TalentSkillNode, Int>>,
        confluenceNodeList: MutableList<Int>,
        action: (ConnectionNode) -> Unit
    ) {
//        if (curPageNum != null && parentNode.pageNum != curPageNum) {
//            return
//        }
        val enhanceLevel = if (currentConfluenceNode == null) 0
        else getEnhanceLevel(parentNode, currentConfluenceNode, enhancedNodeList)
        val preNodeList = parentNode.preNodeList
        if (preNodeList.isEmpty()) {
            action(ConnectionNode(parentNode, null, enhanceLevel))
            return
        }
        if (preNodeList.size > 1) {
            confluenceNodeList.add(parentNode.nodeId)
        }
        preNodeList.forEach { preNode ->
            val isConfluenceNode = confluenceNodeList.contains(preNode.nodeId)
            action(ConnectionNode(parentNode, preNode, enhanceLevel))
            if (!isConfluenceNode) {
                nodeRecursive(
//                    curPageNum,
                    preNode,
                    currentConfluenceNode,
                    enhancedNodeList,
                    confluenceNodeList,
                    action
                )
            }
        }
    }

    private fun getChildrenConfluenceNode(node: TalentSkillNode): TalentSkillNode {
        var childrenConfluenceNode = node
        while (childrenConfluenceNode.preNodeList.size == 1) {
            childrenConfluenceNode = childrenConfluenceNode.preNodeList[0]
        }
        if (childrenConfluenceNode.preNodeList.isEmpty() && childrenConfluenceNode.nodeId != 1) {
            val prevPage = pagedTalentSkillNode[childrenConfluenceNode.pageNum - 1]
            if (prevPage != null) {
                childrenConfluenceNode = prevPage[0]
            }
        }
        return childrenConfluenceNode
    }

    private fun getParentConfluenceNode(node: TalentSkillNode): TalentSkillNode {
        var parentConfluenceNode = node
        val currentPageNodeList = pagedTalentSkillNode[node.pageNum]
        if (currentPageNodeList != null) {
            parentConfluenceNode = currentPageNodeList[0]
            var currentNode = parentConfluenceNode
            val allCurrentPageConfluenceNodes = mutableListOf<TalentSkillNode>()
            while (currentNode.preNodeList.isNotEmpty()) {
                if (currentNode.preNodeList.size > 1) {
                    allCurrentPageConfluenceNodes.add(currentNode)
                }
                currentNode = currentNode.preNodeList[0]
            }
            val validNodes = allCurrentPageConfluenceNodes.filter { it.nodeId > node.nodeId }
            val validNode = validNodes.minByOrNull { it.nodeId }
            if (validNode != null) {
                parentConfluenceNode = validNode
            }
        }
        return parentConfluenceNode
    }

    private fun getInvalidNodeList(
        parentConfluenceNode: TalentSkillNode,
        enhanceNode: TalentSkillNode
    ): List<TalentSkillNode> {
        var invalidIndex = 0
        var index = 0
        outer@ while (index < parentConfluenceNode.preNodeList.size) {
            var node = parentConfluenceNode.preNodeList[index]
            if (node.nodeId == enhanceNode.nodeId) {
                invalidIndex = index
                break@outer
            }
            while (node.preNodeList.size == 1) {
                node = node.preNodeList[0]
                if (node.nodeId == enhanceNode.nodeId) {
                    invalidIndex = index
                    break@outer
                }
            }
            index++
        }
        val invalidNodeList = mutableListOf<TalentSkillNode>()
        var node = parentConfluenceNode.preNodeList[invalidIndex]
        invalidNodeList.add(node)
        while (node.preNodeList.size == 1) {
            node = node.preNodeList[0]
            invalidNodeList.add(node)
        }

        return invalidNodeList
    }

    private fun getEnhanceLevel(
        node: TalentSkillNode,
        currentConfluenceNode: TalentSkillNode,
        enhancedNodeList: List<Pair<TalentSkillNode, Int>>
    ): Int {
        if (node.nodeId < currentConfluenceNode.nodeId || enhancedNodeList.isEmpty()) {
            return node.maxLevel
        }
        val enhancedNode = enhancedNodeList.find { it.first.nodeId == node.nodeId }
        if (enhancedNode != null) {
            return enhancedNode.second
        }
        if (node.nodeId == currentConfluenceNode.nodeId) {
            return node.maxLevel
        }
        enhancedNodeList.forEach { item ->
            var preNode = item.first
            while (preNode.preNodeList.isNotEmpty() && preNode.nodeId > currentConfluenceNode.nodeId) {
                preNode = preNode.preNodeList[0]
                if (preNode.nodeId == node.nodeId) {
                    return node.maxLevel
                }
            }
        }
        return 0
    }

    private fun totalTalentEnhance() {
        val enhancedMap = mutableMapOf<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>()
        val consumeMap = mutableMapOf<Int/*itemId*/, Int>()
        enhancedMap.putAll(talentLevelEnhancedMap)
        consumeMap.putAll(talentLevelConsumeMap)
        talentSkillEnhancedMap.forEach { (key, value) ->
            enhancedMap.mergeIntValue(key, value)
        }
        talentSkillConsumeMap.forEach { (key, value) ->
            consumeMap.mergeIntValue(key, value)
        }
        teamSkillEnhancedMap.forEach { (key, value) ->
            enhancedMap.mergeIntValue(key, value)
        }
        teamSkillConsumeMap.forEach { (key, value) ->
            consumeMap.mergeIntValue(key, value)
        }
        groupedTalentEnhancedMap = enhancedMap.groupByFirstThenSortBySecond()
        talentConsumeMap = consumeMap
    }

    private fun totalTalentLevelEnhance() {
        if (talentLevelMap.isNullOrEmpty()) {
            groupedTalentLevelEnhancedMap = emptyMap()
            talentLevelEnhancedMap = emptyMap()
            talentLevelConsumeMap = emptyMap()
        } else {
            val enhancedMap = mutableMapOf<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>()
            val consumeMap = mutableMapOf<Int/*itemId*/, Int>()
            val parameterType = SkillNode.TalentAtk.parameterType
            talentLevelMap!!.forEach { (talentId, pair) ->
                val expTalentLevel = pair.first
                enhancedMap.mergeIntValue(talentId to parameterType, expTalentLevel.totalEnhanceValue)
                consumeMap.mergeIntValue(25010 + talentId, expTalentLevel.totalPoint)
            }
            groupedTalentLevelEnhancedMap = enhancedMap.groupByFirstThenSortBySecond()
            talentLevelEnhancedMap = enhancedMap
            talentLevelConsumeMap = consumeMap
        }
    }

    private fun totalTalentSkillEnhance() {
        if (enhancedTalentSkillNodeList.isEmpty()) {
            groupedTalentSkillEnhancedMap = emptyMap()
            talentSkillEnhancedMap = emptyMap()
            talentSkillConsumeMap = emptyMap()
        } else {
            val enhancedMap = mutableMapOf<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>()
            val consumeMap = mutableMapOf<Int/*itemId*/, Int>()
            val pageNum = enhancedTalentSkillNodeList[0].first.pageNum
            val lastPageNodeList = pagedTalentSkillNode[pageNum]!!
            val parentNode = lastPageNodeList[0]
            val connections = mutableListOf<ConnectionNode>()
            forEachConnection(parentNode, enhancedTalentSkillNodeList) { connectionNode ->
                connections.add(connectionNode)
            }
            val enhancedNodes = connections.distinctBy { it.node.nodeId }
            enhancedNodes.forEach { enhancedNode ->
                if (enhancedNode.enhanceLevel > 0) {
                    val enhanceLevelList = enhancedNode.node.enhanceLevelList
                    enhanceLevelList.forEachIndexed { index, item ->
                        if (index < enhancedNode.enhanceLevel) {
                            consumeMap.mergeIntValue(item.itemId, item.consumeNum)
                        }
                    }
                    enhanceLevelList[enhancedNode.enhanceLevel - 1].enhanceDataList.forEach { enhanceData ->
                        enhanceData.talentIdList.forEach { talentId ->
                            enhancedMap.mergeIntValue(talentId to enhanceData.parameterType, enhanceData.enhanceValue)
                        }
                    }
                }
            }
            if (pageNum > 1) {
                for (page in 1 until pageNum) {
                    val pageNodeList = pagedTalentSkillNode[page]!!
                    pageNodeList.forEach { node ->
                        val enhanceLevelList = node.enhanceLevelList
                        enhanceLevelList.forEach { item ->
                            consumeMap.mergeIntValue(item.itemId, item.consumeNum)
                        }
                        enhanceLevelList[enhanceLevelList.size - 1].enhanceDataList.forEach { enhanceData ->
                            enhanceData.talentIdList.forEach { talentId ->
                                enhancedMap.mergeIntValue(talentId to enhanceData.parameterType, enhanceData.enhanceValue)
                            }
                        }
                    }
                }
            }
            groupedTalentSkillEnhancedMap = enhancedMap.groupByFirstThenSortBySecond()
            talentSkillEnhancedMap = enhancedMap
            talentSkillConsumeMap = consumeMap
        }
    }

    private fun totalTeamSkillEnhance() {
        if (enhancedTeamSkillNode == null) {
            groupedTeamSkillEnhancedMap = emptyMap()
            teamSkillEnhancedMap = emptyMap()
            teamSkillConsumeMap = emptyMap()
        } else {
            val enhancedMap = mutableMapOf<Pair<Int/*talentId*/, Int/*parameterType*/>, Int>()
            val consumeMap = mutableMapOf<Int/*itemId*/, Int>()
            var node = enhancedTeamSkillNode
            while (node != null) {
                val enhanceLevel = node.enhanceLevelList[0]
                consumeMap.mergeIntValue(enhanceLevel.itemId, enhanceLevel.consumeNum)
                enhanceLevel.enhanceDataList.forEach { enhanceData ->
                    enhanceData.talentIdList.forEach { talentId ->
                        enhancedMap.mergeIntValue(talentId to enhanceData.parameterType, enhanceData.enhanceValue)
                    }
                }
                node = node.preNode
            }
            groupedTeamSkillEnhancedMap = enhancedMap.groupByFirstThenSortBySecond()
            teamSkillEnhancedMap = enhancedMap
            teamSkillConsumeMap = consumeMap
        }
    }

    private fun totalRoleEnhance() {
        if (roleEnhanceMap == null) {
            groupedRoleEnhancedMap = emptyMap()
            roleConsumeMapEntries = emptyList()
//            roleEnhancedMap = emptyMap()
//            roleConsumeMap = emptyMap()
        } else {
            val enhancedMap = mutableMapOf<Pair<Int/*roleId*/, Int/*parameterType*/>, Int>()
            val consumeMap = mutableMapOf<Int/*itemId*/, Int>()
            val roleIdMap = roleMasteryDataMap!!
            roleEnhanceMap!!.forEach { (roleId, slotIdEnhanceMap) ->
                val slotIdMap = roleIdMap[roleId]!!.slotIdMap
                slotIdEnhanceMap.forEach { (slotId, pair) ->
                    val (slotLevel, enhanceLevel) = pair
                    if (slotLevel > 0) {
                        val slotLevelMap = slotIdMap[slotId]!!.slotLevelMap
                        val typeValuePairList = slotLevelMap[slotLevel]!!.enhanceLevelMap[enhanceLevel]!!.typeValuePairList
                        typeValuePairList.forEach { item ->
                            enhancedMap.mergeIntValue(roleId to item.first, item.second)
                        }
                        for (slotLv in 1..slotLevel) {
                            val roleSlotLevel = slotLevelMap[slotLv]!!
                            val enhanceLevelMap = roleSlotLevel.enhanceLevelMap
                            val maxLevel = if (slotLv == slotLevel) enhanceLevel else roleSlotLevel.maxEnhanceLevel
                            for (enhanceLv in 0..maxLevel) {
                                val roleEnhanceLevel = enhanceLevelMap[enhanceLv]!!
                                consumeMap.mergeIntValue(roleEnhanceLevel.itemId, roleEnhanceLevel.num)
                            }
                        }
                    }
                }
            }
            groupedRoleEnhancedMap = enhancedMap.groupByFirstThenSortBySecond()
            roleConsumeMapEntries = consumeMap.entries.toList()
//            roleEnhancedMap = enhancedMap
//            roleConsumeMap = consumeMap
        }
    }
}

private fun <K> MutableMap<K, Int>.mergeIntValue(key: K, value: Int) {
    val oldValue = this[key]
    if (oldValue == null) {
        this[key] = value
    } else {
        this[key] = oldValue + value
    }
}

private fun <A : Comparable<A>, B : Comparable<B>, C> Map<Pair<A, B>, C>.groupByFirstThenSortBySecond(): Map<A, List<Pair<B, C>>> =
    this.entries
        .groupBy({ it.key.first }, { it.key.second to it.value })
        .mapValues { (_, list) -> list.sortedBy { it.first } }

data class ConnectionNode(
    val node: TalentSkillNode,
    val preNode: TalentSkillNode?,
    val enhanceLevel: Int
)
