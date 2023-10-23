package com.kasuminotes.db

import com.kasuminotes.data.RfSkillData
import com.kasuminotes.data.SkillAction
import com.kasuminotes.data.SkillData
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun AppDatabase.getUnitAttackPatternList(unitId: Int): List<UnitAttackPattern> {
    val sql = """SELECT ${UnitAttackPattern.getFields()}
FROM unit_attack_pattern WHERE unit_id=$unitId ORDER BY pattern_id ASC"""

    return safelyUse {
        rawQuery(sql, null).use {
            val list = mutableListOf<UnitAttackPattern>()

            while (it.moveToNext()) {
                var atkPatternList = mutableListOf<Int>()
                var i = 0

                while (i < 20) {
                    // 删除第14动
                    if (i == 13) {
                        i++
                        continue
                    }

                    val atkPattern = it.getInt(i++)
                    if (atkPattern == 0) break
                    atkPatternList.add(atkPattern)
                }

                i = 20

                val patternId = it.getInt(i++)
                var loopStart = it.getInt(i++)
                var loopEnd = it.getInt(i)

                // 如果删除了第14动，14动后的循坏开始和结束就应该相应地减1
                if (loopStart > 13) {
                    loopStart -= 1
                }
                if (loopEnd > 13) {
                    loopEnd -= 1
                }

                if (atkPatternList.size > loopEnd) {
                    atkPatternList = atkPatternList.subList(0, loopEnd)
                }

                list.add(
                    UnitAttackPattern(
                        patternId,
                        loopStart,
                        loopEnd,
                        atkPatternList
                    )
                )
            }

            list
        }
    }
}

suspend fun AppDatabase.getSkillAction(actionId: Int): SkillAction {
    val sql = """SELECT ${SkillAction.getFields()}
FROM skill_action WHERE action_id=$actionId"""

    return safelyUse {
        rawQuery(sql ,null).use {
            it.moveToFirst()
            var i = 0

            SkillAction(
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getDouble(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getInt(i++),
                it.getString(i)
            )
        }
    }
}

suspend fun AppDatabase.getSkillData(skillId: Int): SkillData? {
    if (skillId == 0) return null

    val sql = "SELECT ${SkillData.getFields()} FROM skill_data WHERE skill_id=$skillId"

    return withIOContext {
        val skillData = use {
            rawQuery(sql, null).use {
                if (it.moveToFirst()) {
                    var i = 0

                    val rawActions = mutableListOf<Int>()
                    val rawDepends = mutableListOf<Int>()

                    while (i < 20) {
                        val actionId = it.getInt(i++)
                        if (actionId == 0) break

                        rawActions.add(actionId)
                        rawDepends.add(it.getInt(i++))
                    }

                    i = 20

                    SkillData(
                        it.getInt(i++),
                        it.getString(i++),
                        it.getString(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getInt(i++),
                        it.getFloat(i++),
                        it.getFloat(i),
                        rawActions,
                        rawDepends,
                        emptyList(),
                        null,
                        false
                    )
                } else {
                    null
                }
            }
        }

        if (skillData == null) {
            null
        } else {
            val actions = skillData.rawActions.map { actionId ->
                async { getSkillAction(actionId) }
            }.awaitAll()

            skillData.copy(actions = actions)
        }
    }
}

private suspend fun AppDatabase.getRfSkillData(skillId: Int): RfSkillData? {
    if (skillId == 0) return null

    return withIOContext {
        if (existsTable("unit_skill_data_rf")) {
            val sql = """SELECT rf_skill_id,min_lv,max_lv
FROM unit_skill_data_rf WHERE skill_id=$skillId"""

            val rawRfSkillData = use {
                rawQuery(sql, null).use {
                    if (it.moveToFirst()) {
                        RawRfSkillData(
                            it.getInt(0),
                            it.getInt(1),
                            it.getInt(2)
                        )
                    } else {
                        null
                    }
                }
            }
            rawRfSkillData?.let { raw ->
                val rfSkill = getSkillData(raw.rfSkillId)
                rfSkill?.let {
                    RfSkillData(raw.minLv, raw.maxLv, it.copy(isRfSkill = true))
                }
            }
        } else {
            null
        }
    }
}

suspend fun AppDatabase.getUnitSkillData(unitId: Int): UnitSkillData {
    val sql = """SELECT ${UnitSkillData.getFields()}
FROM unit_skill_data WHERE unit_id=$unitId"""

    return withIOContext {
        val raw = use {
            rawQuery(sql, null).use {
                it.moveToFirst()

                val getSkillList: (Int, Int) -> List<Int> = { start, end ->
                    val list = mutableListOf<Int>()
                    var i = start
                    while (i < end) {
                        val skillId = it.getInt(i++)
                        if (skillId == 0) break
                        list.add(skillId)
                    }
                    list
                }

                val mainSkillList = getSkillList(0, 10)
                val mainSkillEvolutionList = getSkillList(10, 12)
                val spSkillList = getSkillList(12, 17)
                val spSkillEvolutionList = getSkillList(17, 19)
                val exSkillList = getSkillList(19, 24)
                val exSkillEvolutionList = getSkillList(24, 29)

                var i = 29

                RawUnitSkillData(
                    it.getInt(i++),
                    it.getInt(i++),
                    it.getInt(i),
                    mainSkillList,
                    mainSkillEvolutionList,
                    spSkillList,
                    spSkillEvolutionList,
                    exSkillList,
                    exSkillEvolutionList
                )
            }
        }

        val getDeferredSkillDataList: suspend (List<Int>) -> Deferred<List<SkillData?>> = { list ->
            async {
                list.map { skillId ->
                    val skillAndRfSkill = awaitAll(
                        async { getSkillData(skillId) },
                        async { getRfSkillData(skillId) }
                    )
                    val skillData = skillAndRfSkill[0] as SkillData?
                    val rfSkillData = skillAndRfSkill[1] as RfSkillData?
                    skillData?.copy(rfSkillData = rfSkillData)
                }
            }
        }

        val list = listOf(
            getDeferredSkillDataList(listOf(raw.unionBurst, raw.spUnionBurst, raw.unionBurstEvolution)),
            getDeferredSkillDataList(raw.mainSkillList),
            getDeferredSkillDataList(raw.spSkillList),
            getDeferredSkillDataList(raw.exSkillList),
            getDeferredSkillDataList(raw.mainSkillEvolutionList),
            getDeferredSkillDataList(raw.spSkillEvolutionList),
            getDeferredSkillDataList(raw.exSkillEvolutionList)
        ).awaitAll()

        UnitSkillData(
            list[0][0],
            list[0][1],
            list[0][2],
            list[1],
            list[2],
            list[3],
            list[4],
            list[5],
            list[6]
        )
    }
}

private data class RawRfSkillData(
    val rfSkillId: Int,
    val minLv: Int,
    val maxLv: Int,
)

private data class RawUnitSkillData(
    val unionBurst: Int,
    val spUnionBurst: Int,
    val unionBurstEvolution: Int,
    val mainSkillList: List<Int>,//1-10
    val spSkillList: List<Int>,//1-5
    val exSkillList: List<Int>,//1-5
    val mainSkillEvolutionList: List<Int>,//1-2
    val spSkillEvolutionList: List<Int>,//1-2
    val exSkillEvolutionList: List<Int>//1-5
)
