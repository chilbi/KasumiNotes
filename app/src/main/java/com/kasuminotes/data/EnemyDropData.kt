package com.kasuminotes.data

data class EnemyDropData(
    val enemyId: Int,
    val dropGold: Int,
    val enemyRewardData: EnemyRewardData?// drop_reward_id
)
