package com.kasuminotes.data

data class ExEquip5ExceedLimit(
//    val exEquipmentId: Int,
//    val skillGroupId: Int,
//    val statusGroupId: Int,
//    val costGroupId: Int,
    val skillList: List<SkillData?>,//rank 1-5
    val statusList: List<Property>//rank 1-5
)
