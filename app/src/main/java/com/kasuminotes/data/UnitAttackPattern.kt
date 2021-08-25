package com.kasuminotes.data

import com.kasuminotes.db.AppDatabase
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
        hasUnique: Boolean,
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

        // 1->"A", 1001->"Main1", 1002->"Main2", 2001->"SP1", 2002->"SP2"
        if (atkPattern == 1) {
            iconUrl = UrlUtil.getAtkIconUrl(atkType)
            atkLabel = "A"
        } else {
            val n = atkPattern % 10
            val skillData: SkillData?

            if (atkPattern < 2000) {
                if (hasUnique && n == 1) {
                    atkLabel = "Main 1+"
                    skillData = unitSkillData.mainSkillEvolutionList.getOrNull(0)
                } else {
                    atkLabel = "Main $n"
                    skillData = unitSkillData.mainSkillList.getOrNull(n - 1)
                }
            } else {
                if (hasUnique && n == 1) {
                    atkLabel = "SP 1+"
                    skillData = unitSkillData.spSkillEvolutionList.getOrNull(0)
                } else {
                    atkLabel = "SP $n"
                    skillData = unitSkillData.spSkillList.getOrNull(n - 1)
                }
            }

            iconUrl = if (skillData != null) {
                UrlUtil.getSkillIconUrl(skillData.iconType)
            } else {
                UrlUtil.getEquipIconUrl(AppDatabase.NullId)
            }
        }

        return AtkPattern(loopLabel, iconUrl, atkLabel)
    }

    companion object {
        fun getFields(): String {
            val atkPatternFields = (1..20).joinToString(",") { i ->
                "atk_pattern_$i"
            }
            return "$atkPatternFields," +
                    "pattern_id," +
                    "loop_start," +
                    "loop_end"
        }
    }
}
