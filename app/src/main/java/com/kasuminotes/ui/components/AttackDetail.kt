package com.kasuminotes.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.utils.UrlUtil
import kotlin.math.roundToInt

@Composable
fun AttackDetail(
    atkType: Int,
    normalAtkCastTime: Float,
    searchAreaWidth: Int,
    property: Property,
) {
    val name: String
    val damage: Double
    val atkTypeText: String

    if (atkType == 1) {
        name = stringResource(R.string.physical_atk)
        damage = property.atk
        atkTypeText = stringResource(R.string.physical)
    } else {
        name = stringResource(R.string.magic_atk)
        damage = property.magicStr
        atkTypeText = stringResource(R.string.magic)
    }

    val target = stringResource(R.string.target_front) +
            stringResource(R.string.target_enemy_count1, stringResource(R.string.target_count1))

    val description = stringResource(
        R.string.action_damage_target1_formula2_content3,
        target,
        damage.roundToInt().toString(),// TODO 不确定的取整方式
        atkTypeText
    )

    SkillDetail(
        label = "A",
        isRfSkill = false,
        iconUrl = UrlUtil.getAtkIconUrl(atkType),
        name = name,
        coolTime = 0f,
        castTime = normalAtkCastTime,
        description = description,
        searchAreaWidth = searchAreaWidth
    )
}
