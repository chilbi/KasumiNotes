package com.kasuminotes.data

data class UniqueCraft(
    val unlockLevel: Int,
    val heartId: Int,
    val heartSum: Int,
    val memoryIdList: List<Int>,
    val memorySum: Int
)
