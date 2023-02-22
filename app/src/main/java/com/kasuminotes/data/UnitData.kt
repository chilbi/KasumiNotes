package com.kasuminotes.data

data class UnitData(
    val unitId: Int,
    val unitName: String,
    val kana: String,
    val actualName: String,
    val maxRarity: Int,
    val equipId: Int,
    val searchAreaWidth: Int,
    val atkType: Int,
    val normalAtkCastTime: Float,
    val comment: String,
    val startTime: String,
    val age: String,
    val guild: String,
    val race: String,
    val height: String,
    val weight: String,
    val birthMonth: String,
    val birthDay: String,
    val bloodType: String,
    val favorite: String,
    val voice: String,
    val catchCopy: String,
    val selfText: String
) {
    val position = when {
        searchAreaWidth < 300 -> 1
        searchAreaWidth < 600 -> 2
        else -> 3
    }

    val hasUnique: Boolean get() = equipId != 0

    val startTimeStr = startTime.split(" ")[0].substring(2)

    val startTimeInt = startTimeStr.replace("/", "").toInt()
//    val startDateTime: LocalDateTime = LocalDateTime.parse(startTime, formatter)

    companion object {
//        private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss")

        private var fields: String? = null

        fun getFields(pk: Boolean): String {
            if (fields == null) {
                fields = "unit_name," +
                        "kana," +
                        "actual_name," +
                        "max_rarity," +
                        "equip_id," +
                        "search_area_width," +
                        "atk_type," +
                        "normal_atk_cast_time," +
                        "comment," +
                        "start_time," +
                        "age," +
                        "guild," +
                        "race," +
                        "height," +
                        "weight," +
                        "birth_month," +
                        "birth_day," +
                        "blood_type," +
                        "favorite," +
                        "voice," +
                        "catch_copy," +
                        "self_text"
            }
            return (if (pk) "unit_id," else "") + fields
        }
    }
}
