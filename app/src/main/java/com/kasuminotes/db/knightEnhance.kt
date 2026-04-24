package com.kasuminotes.db

import com.kasuminotes.data.ExperienceTalentLevel
import com.kasuminotes.data.RoleEnhanceLevel
import com.kasuminotes.data.RoleMasteryData
import com.kasuminotes.data.RoleSlotData
import com.kasuminotes.data.RoleSlotLevel
import com.kasuminotes.data.SkillEnhanceData
import com.kasuminotes.data.SkillEnhanceLevel
import com.kasuminotes.data.TalentSkillNode
import com.kasuminotes.data.TeamSkillNode
import kotlin.collections.forEach

fun AppDatabase.getMaxTalentLevel(): Int? {
    if (!existsTable("experience_talent_level")) {
        return null
    }
    return useDatabase {
        rawQuery("SELECT talent_level FROM experience_talent_level ORDER BY talent_level DESC LIMIT 1", null).use {
            if (it.moveToFirst()) {
                it.getInt(0)
            } else {
                null
            }
        }
    }
}

fun AppDatabase.getTalentLevelPair(talentLevel: Int, toTalentLevel: Int): Pair<ExperienceTalentLevel, ExperienceTalentLevel>? {
    if (!existsTable("experience_talent_level")) {
        return null
    }
    return useDatabase {
        rawQuery("SELECT talent_level,total_point,total_enhance_value FROM experience_talent_level WHERE talent_level in ($talentLevel,$toTalentLevel) ORDER BY talent_level ASC", null).use {
            val list = mutableListOf<ExperienceTalentLevel>()
            while (it.moveToNext()) {
                list.add(ExperienceTalentLevel(
                    it.getInt(0),
                    it.getInt(1),
                    it.getInt(2)
                ))
            }
            if (list.isEmpty()) {
                null
            } else if (list.size == 1) {
                list[0] to list[0]
            } else {
                list[0] to list[1]
            }
        }
    }
}

fun AppDatabase.getTalentSkillNodeList(): List<TalentSkillNode>? {
    if (!existsTables(listOf("talent_skill_node", "talent_skill_enhance_level", "talent_skill_enhance_data"))) {
        return null
    }

    val talentFields = (1..5).joinToString(",") { i -> "talent_id_$i" }
    val preNodeFields = (1..5).joinToString(",") { i -> "pre_node_$i" }
    val enhanceLevelIdFields = (1..5).joinToString(",") { i -> "tsn.enhance_level_id_$i" }
    val enhanceIdFields = (1..5).joinToString(",") { i -> "tsel.enhance_id_$i" }
    val sql = """SELECT node_id,enhance_level_id,enhance_id,icon_id,title_id,page_num,pos_x,pos_y,item_id,consume_num,parameter_type,enhance_value,$talentFields,$preNodeFields
FROM talent_skill_node AS tsn
JOIN talent_skill_enhance_level AS tsel ON tsel.enhance_level_id IN ($enhanceLevelIdFields)
JOIN talent_skill_enhance_data AS tsed ON tsed.enhance_id IN ($enhanceIdFields)
ORDER BY node_id DESC"""

    return useDatabase {
        val rawList = rawQuery(sql, null).use {
            val list = mutableListOf<RawTalentSkillNode>()
            while (it.moveToNext()) {
                var i = 0
                list.add(RawTalentSkillNode(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    List(5) { _ -> it.getInt(i++) },
                    List(5) { _ -> it.getInt(i++) }
                ))
            }
            list
        }
        val nodeList: List<TalentSkillNode> = rawList
            .groupBy { item -> item.nodeId }
            .map { nodeIdEntry ->
                val enhanceLevelList = nodeIdEntry.value
                    .groupBy { item -> item.enhanceLevelId }
                    .map { enhanceLevelIdEntry ->
                        val enhanceDataList = enhanceLevelIdEntry.value.map { item ->
                            SkillEnhanceData(
                                item.enhanceId,
                                item.parameterType,
                                item.enhanceValue,
                                item.talentIdList
                            )
                        }
                        val firstItem = enhanceLevelIdEntry.value.first()
                        SkillEnhanceLevel(
                            firstItem.enhanceLevelId,
                            firstItem.itemId,
                            firstItem.consumeNum,
                            enhanceDataList
                        )
                    }
                val firstItem = nodeIdEntry.value.first()
                TalentSkillNode(
                    firstItem.nodeId,
                    firstItem.iconId,
                    firstItem.titleId,
                    firstItem.pageNum,
                    firstItem.posX,
                    firstItem.posY,
                    enhanceLevelList,
                    firstItem.preNodeIdList,
                    emptyList()
                )
            }
        nodeList.forEach { node ->
            val preNodeIdList = node.preNodeIdList.filter { id -> id != 0 }
            if (preNodeIdList.isNotEmpty()) {
                val preNodeList = mutableListOf<TalentSkillNode>()
                preNodeIdList.forEach { preNodeId ->
                    val preNode = nodeList.find { item -> item.nodeId == preNodeId }
                    if (preNode != null) {
                        preNodeList.add(preNode)
                    }
                }
                node.preNodeList = preNodeList
            }
        }
        nodeList
    }
}

fun AppDatabase.getTeamSkillNodeList(): List<TeamSkillNode>? {
    if (!existsTables(listOf("team_skill_node", "team_skill_enhance_level", "team_skill_enhance_data"))) {
        return null
    }

    val talentFields = (1..5).joinToString(",") { i -> "talent_id_$i" }
    val enhanceLevelIdFields = (1..5).joinToString(",") { i -> "tsn.enhance_level_id_$i" }
    val enhanceIdFields = (1..5).joinToString(",") { i -> "tsel.enhance_id_$i" }
    val sql = """SELECT node_id,pre_node_id,enhance_level_id,enhance_id,icon_id,title_id,pos_x,pos_y,item_id,consume_num,parameter_type,enhance_value,$talentFields
FROM team_skill_node AS tsn
JOIN team_skill_enhance_level AS tsel ON tsel.enhance_level_id IN ($enhanceLevelIdFields)
JOIN team_skill_enhance_data AS tsed ON tsed.enhance_id IN ($enhanceIdFields)
ORDER BY node_id DESC"""

    return useDatabase {
        val rawList = rawQuery(sql, null).use {
            val list = mutableListOf<RawTeamSkillNode>()
            while (it.moveToNext()) {
                var i = 0
                list.add(RawTeamSkillNode(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    List(5) { _ -> it.getInt(i++) }
                ))
            }
            list
        }
        val nodeList: List<TeamSkillNode> = rawList
            .groupBy { item -> item.nodeId }
            .map { nodeIdEntry ->
                val enhanceLevelList = nodeIdEntry.value
                    .groupBy { item -> item.enhanceLevelId }
                    .map { enhanceLevelIdEntry ->
                        val enhanceDataList = enhanceLevelIdEntry.value.map { item ->
                            SkillEnhanceData(
                                item.enhanceId,
                                item.parameterType,
                                item.enhanceValue,
                                item.talentIdList
                            )
                        }
                        val firstItem = enhanceLevelIdEntry.value.first()
                        SkillEnhanceLevel(
                            firstItem.enhanceLevelId,
                            firstItem.itemId,
                            firstItem.consumeNum,
                            enhanceDataList
                        )
                    }
                val firstItem = nodeIdEntry.value.first()
                TeamSkillNode(
                    firstItem.nodeId,
                    firstItem.iconId,
                    firstItem.titleId,
                    firstItem.posX,
                    firstItem.posY,
                    enhanceLevelList,
                    firstItem.preNodeId,
                    null
                )
            }
        nodeList.forEach { node ->
            val preNodeId = node.preNodeId
            if (preNodeId != 0) {
                val preNode = nodeList.find { item -> item.nodeId == preNodeId }
                if (preNode != null) {
                    node.preNode = preNode
                }
            }
        }
        nodeList
    }
}

fun AppDatabase.getRoleMasteryDataMap(): Map<Int, RoleMasteryData>? {
    if (!existsTables(listOf(
            "unit_role_mastery_id",
            "unit_role_mastery_slot_data",
            "unit_role_mastery_item_data",
            "unit_role_mastery_level",
            "unit_role_mastery_enhance_data"
    ))) {
        return null
    }

    val sql = """SELECT i.mastery_id,unit_role_id,slot_id,
sd.slot_level,icon_id,item_id_1,num,l.enhance_level,
role_param_type_1,role_param_type_2,enhance_value_1,enhance_value_2
FROM unit_role_mastery_id AS i
JOIN unit_role_mastery_slot_data AS sd ON sd.mastery_id=i.mastery_id
JOIN unit_role_mastery_item_data AS id ON id.mastery_id=sd.mastery_id AND id.slot_level=sd.slot_level
JOIN unit_role_mastery_level AS l ON l.mastery_id=sd.mastery_id AND l.slot_level=sd.slot_level
JOIN unit_role_mastery_enhance_data AS ed ON ed.mastery_id=l.mastery_id AND ed.slot_level=l.slot_level AND ed.enhance_level=l.enhance_level"""

    return useDatabase {
        val rawList = rawQuery(sql, null).use {
            val list = mutableListOf<RawRoleMasteryData>()
            while (it.moveToNext()) {
                var i = 0
                list.add(RawRoleMasteryData(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i)
                ))
            }
            list
        }
        val roleMap = mutableMapOf<Int, RoleMasteryData>()
        rawList
            .groupBy { it.unitRoleId }
            .forEach { unitRoleIdEntry ->
                val (unitRoleId, groupByUnitRoleIdList) = unitRoleIdEntry
                val slotIdMap = mutableMapOf<Int, RoleSlotData>()
                groupByUnitRoleIdList
                    .groupBy { it.slotId }
                    .forEach { slotIdEntry ->
                        val (slotId, groupBySlotIdList) = slotIdEntry
                        val slotLevelMap = mutableMapOf<Int, RoleSlotLevel>()
                        groupBySlotIdList
                            .groupBy { it.slotLevel }
                            .forEach { slotLevelEntry ->
                                val (slotLevel, groupBySlotLevelList) = slotLevelEntry
                                val enhanceLevelMap = mutableMapOf<Int, RoleEnhanceLevel>()
                                groupBySlotLevelList.forEach { item ->
                                    val enhanceLevel = item.enhanceLevel
                                    val typeValuePairList = mutableListOf<Pair<Int, Int>>()
                                    if (item.roleParamType1 != 0) {
                                        typeValuePairList.add(item.roleParamType1 to item.roleEnhanceValue1)
                                    }
                                    if (item.roleParamType2 != 0) {
                                        typeValuePairList.add(item.roleParamType2 to item.roleEnhanceValue2)
                                    }
                                    enhanceLevelMap[enhanceLevel] = RoleEnhanceLevel(
                                        enhanceLevel,
                                        item.itemId,
                                        item.num,
                                        typeValuePairList
                                    )
                                }
                                val firstSlotLevelListItem = groupBySlotLevelList.first()
                                slotLevelMap[slotLevel] = RoleSlotLevel(
                                    slotLevel,
                                    firstSlotLevelListItem.iconId,
                                    enhanceLevelMap
                                )
                            }
//                        val firstSlotIdListItem = groupBySlotIdList.first()
                        slotIdMap[slotId] = RoleSlotData(
                            slotId,
//                            firstSlotIdListItem.iconId,
//                            firstSlotIdListItem.itemId,
                            slotLevelMap
                        )
                    }
                val firstUnitRoleIdListItem = groupByUnitRoleIdList.first()
                roleMap[unitRoleId] = RoleMasteryData(
                    firstUnitRoleIdListItem.masteryId,
                    unitRoleId,
                    slotIdMap
                )
            }
        roleMap
    }
}

private data class RawTalentSkillNode(
    val nodeId: Int,
    val enhanceLevelId: Int,
    val enhanceId: Int,
    val iconId: Int,
    val titleId: Int,
    val pageNum: Int,
    val posX: Int,
    val posY: Int,
    val itemId: Int,
    val consumeNum: Int,
    val parameterType: Int,
    val enhanceValue: Int,
    val talentIdList: List<Int>,
    val preNodeIdList: List<Int>
)

private data class RawTeamSkillNode(
    val nodeId: Int,
    val preNodeId: Int,
    val enhanceLevelId: Int,
    val enhanceId: Int,
    val iconId: Int,
    val titleId: Int,
    val posX: Int,
    val posY: Int,
    val itemId: Int,
    val consumeNum: Int,
    val parameterType: Int,
    val enhanceValue: Int,
    val talentIdList: List<Int>
)

private data class RawRoleMasteryData(
    val masteryId: Int,
    val unitRoleId: Int,
    val slotId: Int,
    val slotLevel: Int,
    val iconId: Int,
    val itemId: Int,
    val num: Int,
    val enhanceLevel: Int,
    val roleParamType1: Int,
    val roleParamType2: Int,
    val roleEnhanceValue1: Int,
    val roleEnhanceValue2: Int
)
