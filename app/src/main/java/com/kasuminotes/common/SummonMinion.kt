package com.kasuminotes.common

import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillItem
import com.kasuminotes.data.UnitAttackPattern
import com.kasuminotes.data.UnitSkillData

interface SummonMinion {
    val unitId: Int
    val enemyId: Int?
    val name: String
    val searchAreaWidth: Int
    val atkType: Int
    val normalAtkCastTime: Float
    val property: Property
    val unitAttackPatternList: List<UnitAttackPattern>
    val unitSkillData: UnitSkillData?
    val skillList: List<SkillItem>
}
