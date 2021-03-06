package com.kasuminotes.data

data class SkillData(
    val skillId: Int,
    val name: String,
    val description: String,
    val iconType: Int,
    val skillType: Int,
    val skillAreaWidth: Int,
    val skillCastTime: Float,
    val bossUbCoolTime: Float,
    val rawActions: List<Int>,//1-7
    val rawDepends: List<Int>,//1-7
    val actions: List<SkillAction>
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val actionFields = (1..7).joinToString(",") { i ->
                    "action_$i,depend_action_$i"
                }
                fields = "$actionFields," +
                        "skill_id," +
                        "name," +
                        "description," +
                        "icon_type," +
                        "skill_type," +
                        "skill_area_width," +
                        "skill_cast_time," +
                        "boss_ub_cool_time"
            }

            return fields!!
        }
    }
}
