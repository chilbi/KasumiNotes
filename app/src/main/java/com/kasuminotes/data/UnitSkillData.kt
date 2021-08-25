package com.kasuminotes.data

import kotlin.math.max

data class UnitSkillData(
    val unionBurst: SkillData?,
    val spUnionBurst: SkillData?,
    val unionBurstEvolution: SkillData?,
    val mainSkillList: List<SkillData?>,//1-10
    val mainSkillEvolutionList: List<SkillData?>,//1-2
    val spSkillList: List<SkillData?>,//1-5
    val spSkillEvolutionList: List<SkillData?>,//1-2
    val exSkillList: List<SkillData?>,//1-5
    val exSkillEvolutionList: List<SkillData?>//1-5
) {
    fun getSkillList(level: Int) = getSkillList(level, level, level, level)

    fun getSkillList(
        ubLevel: Int,
        skill1Level: Int,
        skill2Level: Int,
        exLevel: Int
    ): List<SkillItem> {
        val list = mutableListOf<SkillItem>()

        unionBurst?.let {
            list.add(SkillItem("UB", ubLevel, it))
        }
        unionBurstEvolution?.let {
            list.add(SkillItem("UB+", ubLevel, it))
        }

        val mainSize = max(mainSkillList.size, mainSkillEvolutionList.size)

        if (mainSize > 0) {
            for (i in 0 until mainSize) {
                val level = when (i) {
                    0 -> skill1Level
                    1 -> skill2Level
                    else -> ubLevel
                }
                mainSkillList.getOrNull(i)?.let {
                    list.add(SkillItem("Main ${i + 1}", level, it))
                }
                mainSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem("Main ${i + 1}+", level, it))
                }
            }
        }

        spUnionBurst?.let {
            list.add(SkillItem("SP UB", ubLevel, it))
        }

        val spSize = max(spSkillList.size, spSkillEvolutionList.size)

        if (spSize > 0) {
            for (i in 0 until spSize) {
                spSkillList.getOrNull(i)?.let {
                    list.add(SkillItem("SP ${i + 1}", ubLevel, it))
                }
                spSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem("SP ${i + 1}+", ubLevel, it))
                }
            }
        }

        val exSize = max(exSkillList.size, exSkillEvolutionList.size)

        if (exSize > 0) {
            for (i in 0 until exSize) {
                exSkillList.getOrNull(i)?.let {
                    list.add(SkillItem(if (exSize == 1) "EX" else "EX ${i + 1}", exLevel, it))
                }
                exSkillEvolutionList.getOrNull(i)?.let {
                    list.add(SkillItem(if (exSize == 1) "EX+" else "EX ${i + 1}+", exLevel, it))
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
