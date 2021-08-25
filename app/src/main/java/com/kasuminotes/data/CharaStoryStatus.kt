package com.kasuminotes.data

import com.kasuminotes.utils.Helper

data class CharaStoryStatus(
    val status: List<Property>,
    val sharedChara: List<Int>
) {
    fun getStoryList(
        unitId: Int,
        rarity: Int,
        loveLevel: Int,
        unitName: String,
        sharedProfiles: List<UserProfile>
    ): List<StoryItem> {
        return List(sharedProfiles.size + 1) { index ->
            if (index > 0) {
                val userProfile = sharedProfiles[index - 1]
                val sharedStatus = userProfile.charaStoryStatus?.status
                val unlockCount = if (sharedStatus == null) {
                    0
                } else {
                    Helper.getStoryUnlockCount(sharedStatus.size, userProfile.userData.loveLevel)
                }
                StoryItem(
                    userProfile.unitData.unitId,
                    userProfile.userData.rarity,
                    userProfile.unitData.unitName,
                    sharedStatus,
                    unlockCount
                )
            } else {
                StoryItem(
                    unitId,
                    rarity,
                    unitName,
                    status,
                    Helper.getStoryUnlockCount(status.size, loveLevel)
                )
            }
        }
    }

    companion object {
        fun getFields(): String {
            val statusFields = (1..5).joinToString(",") { i ->
                "status_type_$i,status_rate_$i"
            }
            val charaFields = (2..10).joinToString(",") { i ->
                "chara_id_$i"
            }
            return "$statusFields,$charaFields"
        }
    }
}
