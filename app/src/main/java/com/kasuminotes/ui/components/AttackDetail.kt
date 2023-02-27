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

    val description = stringResource(
        R.string.action_damage_target1_formula2_content3,
        stringResource(R.string.target_one_content1, stringResource(R.string.target_enemy)),
        damage.roundToInt().toString(),
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
