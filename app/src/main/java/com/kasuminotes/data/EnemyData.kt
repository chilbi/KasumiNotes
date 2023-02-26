package com.kasuminotes.data

data class EnemyData(
    val enemyId: Int,
    val unitId: Int,
    val name: String,
    val searchAreaWidth: Int,
    val atkType: Int,
    val normalAtkCastTime: Float,
    val comment: String,
    val level: Int,
    val unionBurstLevel: Int,
    val mainSkillLvList: List<Int>,// 1-10
    val exSkillLvList: List<Int>,// 1-5
    val multiParts: List<Int>,// 1-5
    val property: Property,
    var EnemyMultiParts: List<EnemyData> = emptyList()
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val mainSkillLvFields = (1..10).joinToString(",") { i ->
                    "main_skill_lv_$i"
                }
                val exSkillLvFields = (1..5).joinToString(",") { i->
                    "ex_skill_lv_$i"
                }
                val childEnemyParameterFields = (1..5).joinToString(",") { i ->
                    "child_enemy_parameter_$i"
                }

                fields = "$mainSkillLvFields,$exSkillLvFields,$childEnemyParameterFields,${Property.getFields()}," +
                        "ep.enemy_id," +
                        "ep.unit_id," +
                        "ep.name," +
                        "search_area_width," +
                        "atk_type," +
                        "normal_atk_cast_time," +
                        "comment," +
                        "level," +
                        "union_burst_level"
            }
            return  fields!!
        }
    }
}
