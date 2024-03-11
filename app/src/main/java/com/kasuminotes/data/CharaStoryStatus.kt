package com.kasuminotes.data

import com.kasuminotes.utils.Helper

data class CharaStoryStatus(
    val status: List<Property>,
    val sharedChara: List<Int>
) {
    fun getStoryList(
        unitId: Int,
        rarity: Int,
        maxRarity: Int,
        loveLevel: Int,
        unitName: String,
        sharedProfiles: List<UserProfile>
    ): List<StoryItem> {
        return List(sharedProfiles.size + 1) { index ->
            if (index > 0) {
                val userProfile = sharedProfiles[index - 1]
                val sharedStatus = userProfile.charaStoryStatus?.status
                val diffCount = Helper.getStoryDiffCount(sharedStatus?.size ?: 0, userProfile.unitData.maxRarity)
                val unlockCount = if (sharedStatus == null) {
                    0
                } else {
                    Helper.getStoryUnlockCount(
                        sharedStatus.size,
                        userProfile.userData.loveLevel,
                        userProfile.unitData.maxRarity,
                        diffCount
                    )
                }
                StoryItem(
                    userProfile.unitData.unitId,
                    userProfile.userData.rarity,
                    userProfile.unitData.unitName,
                    sharedStatus,
                    diffCount,
                    unlockCount
                )
            } else {
                val diffCount = Helper.getStoryDiffCount(status.size, maxRarity)
                val unlockCount = Helper.getStoryUnlockCount(status.size, loveLevel, maxRarity, diffCount)
                StoryItem(
                    unitId,
                    rarity,
                    unitName,
                    status,
                    diffCount,
                    unlockCount
                )
            }
        }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                val statusFields = (1..5).joinToString(",") { i ->
                    "status_type_$i,status_rate_$i"
                }
                val charaFields = (2..20).joinToString(",") { i ->
                    "chara_id_$i"
                }
                fields = "$statusFields,$charaFields"
            }
            return fields!!
        }
    }
}
