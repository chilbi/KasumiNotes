package com.kasuminotes.data

data class EquipCraft(
    val equipmentId: Int,
    val consumeSum: Int,
    val conditionList: List<EquipCraft>?//1-10
) {
    fun getCraftList(): List<EquipCraft> {
        return if (conditionList != null) {
            val list = mutableListOf<EquipCraft>()

            conditionList.forEach { condition ->
                list.addAll(condition.getCraftList())
            }

            list
        } else {
            listOf(this)
        }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = (1..10).joinToString(",") { i ->
                    "condition_equipment_id_$i,consume_num_$i"
                }
            }

            return fields!!
        }
    }
}
