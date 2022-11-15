package com.kasuminotes.data

data class SkillAction(
    val actionId: Int,
    val classId: Int,
    val actionType: Int,
    val actionDetail1: Int,
    val actionDetail2: Int,
    val actionDetail3: Int,
    val actionValue1: Double,
    val actionValue2: Double,
    val actionValue3: Double,
    val actionValue4: Double,
    val actionValue5: Double,
    val actionValue6: Double,
    val actionValue7: Double,
    val targetAssignment: Int,
    val targetArea: Int,
    val targetRange: Int,
    val targetType: Int,
    val targetNumber: Int,
    val targetCount: Int,
    val description: String,
    val levelUpDisp: String,
    var depend: SkillAction? = null
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val actionDetailFields = (1..3).joinToString(",") { i ->
                    "action_detail_$i"
                }
                val actionValueFields = (1..7).joinToString(",") { i ->
                    "action_value_$i"
                }
                fields = "action_id,class_id,action_type," +
                        "$actionDetailFields,$actionValueFields," +
                        "target_assignment,target_area,target_range," +
                        "target_type,target_number,target_count,description,level_up_disp"
            }
            return fields!!
        }
    }
}
