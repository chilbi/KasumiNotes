package com.kasuminotes.data

import com.kasuminotes.common.Label
import kotlin.math.max

data class UnitSkillData(
    val unionBurst: SkillData?,
    val spUnionBurst: SkillData?,
    val unionBurstEvolution: SkillData?,
    val mainSkillList: List<SkillData?>,// 1-10
    val mainSkillEvolutionList: List<SkillData?>,// 1-2
    val spSkillList: List<SkillData?>,// 1-5
    val spSkillEvolutionList: List<SkillData?>,// 1-2
    val exSkillList: List<SkillData?>,// 1-5
    val exSkillEvolutionList: List<SkillData?>// 1-5
) {
    fun getSkillList(level: Int): List<SkillItem> {
        val lvList = List(10) { level }
        return getSkillList(
            unionBurstLevel = level,
            mainSkillLvList = lvList,
            exSkillLvList = lvList
        )
    }

    fun getSkillList(
        ubLevel: Int,
        skill1Level: Int,
        skill2Level: Int,
        exLevel: Int
    ): List<SkillItem> {
        val mainSkillLvList = MutableList(10) { ubLevel }
        mainSkillLvList[0] = skill1Level
        mainSkillLvList[1] = skill2Level

        return getSkillList(
            unionBurstLevel = ubLevel,
            mainSkillLvList = mainSkillLvList,
            exSkillLvList = List(10) { exLevel }
        )
    }

    fun getSkillList(
        unionBurstLevel: Int,
        mainSkillLvList: List<Int>,
        exSkillLvList: List<Int>
    ): List<SkillItem> {
        val list = mutableListOf<SkillItem>()

        unionBurst?.let {
            list.add(SkillItem(Label.ub, unionBurstLevel, it.getSkillOrRfSkill(unionBurstLevel)))
        }
        unionBurstEvolution?.let {
            list.add(SkillItem(Label.ub + "+", unionBurstLevel, it.getSkillOrRfSkill(unionBurstLevel)))
        }

        val mainSize = max(mainSkillList.size, mainSkillEvolutionList.size)

        if (mainSize > 0) {
            for (i in 0 until mainSize) {
                val level = mainSkillLvList[i]
                mainSkillList.getOrNull(i)?.let {
                    list.add(SkillItem(Label.skill + (i + 1).toString(), level, it.getSkillOrRfSkill(level)))
                }
                mainSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem(Label.skill + "${i + 1}+", level, it.getSkillOrRfSkill(level)))
                }
            }
        }

        spUnionBurst?.let {
            list.add(SkillItem(Label.sp + Label.ub, unionBurstLevel, it.getSkillOrRfSkill(unionBurstLevel)))
        }

        val spSize = max(spSkillList.size, spSkillEvolutionList.size)

        if (spSize > 0) {
            for (i in 0 until spSize) {
                spSkillList.getOrNull(i)?.let {
                    list.add(SkillItem(Label.sp + (i + 1).toString(), unionBurstLevel, it.getSkillOrRfSkill(unionBurstLevel)))
                }
                spSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem(Label.sp + "${i + 1}+", unionBurstLevel, it.getSkillOrRfSkill(unionBurstLevel)))
                }
            }
        }

        val exSize = max(exSkillList.size, exSkillEvolutionList.size)

        if (exSize > 0) {
            for (i in 0 until exSize) {
                val level = exSkillLvList[i]
                exSkillList.getOrNull(i)?.let {
                    list.add(SkillItem(if (exSize == 1) Label.ex else Label.ex + (i + 1).toString(), level, it.getSkillOrRfSkill(level)))
                }
                exSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem(if (exSize == 1) Label.ex + "+" else Label.ex + "${i + 1}+", level, it.getSkillOrRfSkill(level)))
                }
            }
        }

        return list
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val mainSkillFields = (1..10).joinToString(",") { i ->
                    "main_skill_$i"
                }
                val mainSkillEvolutionFields = (1..2).joinToString(",") { i ->
                    "main_skill_evolution_$i"
                }

                val spSkillFields = (1..5).joinToString(",") { i ->
                    "sp_skill_$i"
                }
                val spSkillEvolutionFields = (1..2).joinToString(",") { i ->
                    "sp_skill_evolution_$i"
                }

                val exSkillFields = (1..5).joinToString(",") { i ->
                    "ex_skill_$i"
                }
                val exSkillEvolutionFields = (1..5).joinToString(",") { i ->
                    "ex_skill_evolution_$i"
                }

                fields = "$mainSkillFields," +
                        "$mainSkillEvolutionFields," +
                        "$spSkillFields," +
                        "$spSkillEvolutionFields," +
                        "$exSkillFields," +
                        "$exSkillEvolutionFields," +
                        "union_burst," +
                        "sp_union_burst," +
                        "union_burst_evolution"
            }
            return fields!!
        }
    }
}
