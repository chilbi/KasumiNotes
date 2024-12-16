package com.kasuminotes.data

data class AbyssSchedule(
    val abyssId: Int,
    val talentId: Int,
    val title: String,
    val startTime: String,
    val endTime: String
) {
    val startTimeText = startTime.split(" ").getOrElse(0) { startTime }
}
