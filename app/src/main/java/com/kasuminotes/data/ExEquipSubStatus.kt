package com.kasuminotes.data

data class ExEquipSubStatus(
    val status: Int,
    val values: List<Int>//1-5
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val valueFields = (1..5).joinToString(",") { key ->
                    "value_$key"
                }
                fields = "$valueFields,status"
            }
            return fields!!
        }
    }
}
