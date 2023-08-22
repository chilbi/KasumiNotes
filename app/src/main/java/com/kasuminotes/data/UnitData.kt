package com.kasuminotes.data

data class UnitData(
    val unitId: Int,
    val unitName: String,
    val kana: String,
    val actualName: String,
    val maxRarity: Int,
    val equip1Id: Int,
    val equip2Id: Int,
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
    val position: Int = when {
        searchAreaWidth < 300 -> 1
        searchAreaWidth < 600 -> 2
        else -> 3
    }

    val hasUnique1: Boolean get() = equip1Id != 0

    val hasUnique2: Boolean get() = equip2Id != 0

    val startTimeStr: String = startTime
        .split(" ")[0]
        .substring(2)

    val startTimeInt: Int = startTimeStr
        .split("/")
        .joinToString("") { s -> if (s.length < 2) "0$s" else s }
        .toInt()
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
                        "equip1_id," +
                        "equip2_id," +
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
