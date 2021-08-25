package com.kasuminotes.data

data class WaveGroupData(
    val waveGroupId: Int,
    val enemyDropDataList: List<EnemyDropData>//1-5 drop_reward_id
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val enemyDropFields = (1..5).joinToString(",") { i ->
                    "enemy_id_$i,drop_gold_$i,drop_reward_id_$i"
                }
                fields = "$enemyDropFields,wave_group_id"
            }

            return fields!!
        }
    }
}
