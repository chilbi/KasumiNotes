package com.kasuminotes.data

data class ClanBattleMapData(
    val lapNumFrom: Int,
    val lapNumTo: Int,
    val phase: Int,
    val scoreCoefficientList: List<Float>,
    val waveGroupIdList: List<Int>,// 1-5
    var enemyDataList: List<EnemyData> = emptyList()
) {
    val from: Int = if (phase == 1) 1 else lapNumFrom

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val waveGroupIdFields = (1..5).joinToString(",") { i ->
                    "wave_group_id_$i"
                }
                val scoreCoefficientFields = (1..5).joinToString(",") { i ->
                    "score_coefficient_$i"
                }

                fields = "$waveGroupIdFields,$scoreCoefficientFields,lap_num_from,lap_num_to,phase"
            }
            return fields!!
        }
    }
}
