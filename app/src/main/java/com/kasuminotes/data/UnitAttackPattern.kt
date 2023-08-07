package com.kasuminotes.data

import com.kasuminotes.common.Label
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

        // 1->"A", 1001->"S1", 1002->"S2", 2001->"SP1", 2002->"SP2"
        if (atkPattern == 1) {
            iconUrl = UrlUtil.getAtkIconUrl(atkType)
            atkLabel = Label.a
        } else {
            val n = atkPattern % 10
            val skillData: SkillData?

            if (atkPattern < 2000) {
                if (hasUnique && n == 1 && unitSkillData.mainSkillEvolutionList.isNotEmpty()) {
                    atkLabel = Label.skill + "1+"
                    skillData = unitSkillData.mainSkillEvolutionList.getOrNull(0)
                } else {
                    atkLabel = Label.skill + n.toString()
                    skillData = unitSkillData.mainSkillList.getOrNull(n - 1)
                }
            } else {
                if (hasUnique && n == 1 && unitSkillData.spSkillEvolutionList.isNotEmpty()) {
                    atkLabel = Label.sp + "1+"
                    skillData = unitSkillData.spSkillEvolutionList.getOrNull(0)
                } else {
                    atkLabel = Label.sp + n.toString()
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
