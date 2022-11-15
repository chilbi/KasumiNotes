package com.kasuminotes.data

data class EnemyRewardData(
    val dropRewardId: Int,
    val rewardDataList: List<RewardData>//1-5 reward_id
) {
    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = (1..5).joinToString(",") { i ->
                    "reward_id_$i,reward_type_$i,odds_$i"
                }
            }

            return fields!!
        }
    }
}
