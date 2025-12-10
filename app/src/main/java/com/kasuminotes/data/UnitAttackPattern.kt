package com.kasuminotes.data

import com.kasuminotes.common.Label
import com.kasuminotes.utils.Helper
import com.kasuminotes.utils.UrlUtil

data class UnitAttackPattern(
    val patternId: Int,
    val loopStart: Int,
    val loopEnd: Int,
    val atkPatternList: List<Int>//1-20
) {
     inner class AtkPattern(
        val loopLabel: String?,
        val iconUrl: String,
        val atkLabel: String,
    )

    fun getAtkPattern(
        index: Int,
        atkType: Int,
        hasUnique1: Boolean,
        hasUnique2: Boolean,
        unitSkillData: UnitSkillData
    ): AtkPattern {
        val atkPattern = atkPatternList[index]
        val loopLabel: String? = when (index + 1) {
            loopStart -> "START"
            loopEnd -> "END"
            else -> null
        }
        val iconUrl: String
        val atkLabel: String

        // 1->"A", 1001->"S1", 1002->"S2", 2001->"SP1", 2002->"SP2"
        if (atkPattern == 1) {
            iconUrl = UrlUtil.getAtkIconUrl(atkType)
            atkLabel = Label.a
        } else {
            val isSP = atkPattern > 2000
            val n = if (isSP) atkPattern - 2000 else atkPattern - 1000
            var isEvolution = (hasUnique1 && n == 1) || (hasUnique2 && n == 2)
            val skillList = if (isEvolution) {
                if (isSP) {
                    unitSkillData.spSkillEvolutionList.filter { it != null }.ifEmpty {
                        isEvolution = false
                        unitSkillData.spSkillList
                    }
                } else {
                    unitSkillData.mainSkillEvolutionList.filter { it != null }.ifEmpty {
                        isEvolution = false
                        unitSkillData.mainSkillList
                    }
                }
            } else {
                if (isSP) unitSkillData.spSkillList
                else unitSkillData.mainSkillList
            }
            val skillData = skillList.getOrNull(n - 1)
            iconUrl = if (skillData != null) {
                UrlUtil.getSkillIconUrl(skillData.iconType)
            } else {
                UrlUtil.getEquipIconUrl(Helper.NullId)
            }
            var label = if (isSP) Label.sp else Label.skill
            label += n.toString()
            if (isEvolution) label += "+"
            atkLabel = label
        }

        return AtkPattern(loopLabel, iconUrl, atkLabel)
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val atkPatternFields = (1..20).joinToString(",") { i ->
                    "atk_pattern_$i"
                }
                fields = "$atkPatternFields," +
                        "pattern_id," +
                        "loop_start," +
                        "loop_end"
            }
            return fields!!
        }
    }
}
