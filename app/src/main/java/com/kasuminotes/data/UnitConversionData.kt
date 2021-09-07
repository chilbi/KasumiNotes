package com.kasuminotes.data

data class UnitConversionData(
    val convertedUnitId: Int,
    val unitData: UnitData,
    var unitAttackPatternList: List<UnitAttackPattern> = emptyList(),
    var unitSkillData: UnitSkillData? = null,
    var exSkillData: ExSkillData? = null
)
