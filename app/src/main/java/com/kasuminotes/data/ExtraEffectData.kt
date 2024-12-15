package com.kasuminotes.data

data class ExtraEffectData(
    val extraEffectId: Int,
    val setId: Int,
    val contentType: Int,
    val targetValueList: List<Int>,//1-2
    val enemyIdList: List<Int>,//1-5
    val execTimingList: List<Int>//1-5
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val targetValueFields = (1..2).joinToString(",") { i ->
                    "target_value_$i"
                }
                val enemyIdFields = (1..5).joinToString(",") { i->
                    "enemy_id_$i"
                }
                val execTimingFields = (1..5).joinToString(",") { i ->
                    "exec_timing_$i"
                }

                fields = "$targetValueFields,$enemyIdFields,$execTimingFields," +
                        "extra_effect_id," +
                        "set_id," +
                        "content_type"
            }
            return  fields!!
        }
    }
}
