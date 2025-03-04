package com.kasuminotes.ui.app.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.OrderBy
import com.kasuminotes.data.UserProfile
import com.kasuminotes.state.CharaImageState
import com.kasuminotes.ui.components.UnitElement
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
import com.kasuminotes.ui.components.UnitRole
import com.kasuminotes.ui.theme.UnitImageShape
import com.kasuminotes.ui.theme.ShadowColor
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaItem(
    layerAlpha: State<Float>,
    orderBy: OrderBy,
    userProfile: UserProfile,
    charaImageState: CharaImageState,
    onCharaClick: (UserProfile) -> Unit
) {
    SizedBox(charaImageState.ratio) {
        val padding = 2.dp
        val maxRarity = userProfile.unitData.maxRarity
        val rarity = userProfile.userData.rarity
        val hasUnique1 =
            userProfile.unitData.hasUnique1 && userProfile.userData.unique1Level > 0
        val enablePositionAlpha = charaImageState.isIcon && maxRarity > 5

        RarityBorderShadow()

        CharaImage(
            charaImageState.isPlate,
            userProfile.unitData.unitId,
            rarity,
            charaImageState.getImageUrl,
            onClick = { onCharaClick(userProfile) }
        )

        RarityBorder(userProfile.userData.rankRarity)

        Rarities(
            padding,
            layerAlpha,
            hasUnique1,
            maxRarity,
            rarity,
            charaImageState.starSize,
            charaImageState.isIcon,
            charaImageState.star0Id,
            charaImageState.star1Id,
            charaImageState.star6Id
        )

        Position(
            padding,
            layerAlpha,
            enablePositionAlpha,
            userProfile.unitData.position,
            charaImageState.positionSize
        )

        if (hasUnique1) {
            Unique(
                padding,
                layerAlpha,
                charaImageState.uniqueSize,
                charaImageState.uniqueId
            )
        }

        Column(Modifier.align(Alignment.TopStart).padding(start = padding, top = padding)) {
            UnitElement(
                0.dp,
                userProfile.unitData.talentId,
                charaImageState.positionSize
            )
            Spacer(Modifier.size(padding))
            UnitRole(
                0.dp,
                userProfile.unitData.unitRoleId,
                charaImageState.positionSize
            )
        }

        Caption(userProfile.getStringOf(orderBy), layerAlpha, charaImageState)
    }
}

@Composable
private fun BoxScope.Caption(text: String, layerAlpha: State<Float>, charaImageState: CharaImageState) {
    if (text.isNotEmpty()) {
        val style = if (charaImageState.isIcon) {
            MaterialTheme.typography.bodySmall
        } else if (charaImageState.isPlate) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(
            text = text,
            modifier = Modifier
                .padding(4.dp, 2.dp)
                .align(Alignment.TopEnd)
                .graphicsLayer { alpha = layerAlpha.value },
            color = Color.White,
            style = style.copy(shadow = Shadow(offset = Offset(1.0f, 1.0f), blurRadius = 1.5f))
        )
    }
}

@Composable
private fun RarityBorderShadow() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 1.dp)
            .offset(y = 2.5.dp)
            .background(
                color = ShadowColor,
                shape = UnitImageShape
            )
    )
}

@Composable
private fun BoxScope.CharaImage(
    isPlate: Boolean,
    unitId: Int,
    rarity: Int,
    getImageUrl: (Int, Int) -> String,
    onClick: () -> Unit
) {
    PlaceImage(
        // 修复api网站没有环奈的plate图片
        url = if (isPlate && (unitId == 170101 || unitId == 170201)) {
            UrlUtil.getKanNaPlateUrl(unitId, rarity)
        } else {
            getImageUrl(unitId, rarity)
        },
        modifier = Modifier
            .clip(UnitImageShape)
            .clickable { onClick() },
        shape = UnitImageShape
    )
}

@Composable
private fun RarityBorder(rankRarity: Int) {
    Box(Modifier.rarityBorder(rankRarity))
}

@Composable
private fun BoxScope.Rarities(
    padding: Dp,
    layerAlpha: State<Float>,
    hasUnique1: Boolean,
    maxRarity: Int,
    rarity: Int,
    starSize: Dp,
    isIcon: Boolean,
    @DrawableRes star0Id: Int,
    @DrawableRes star1Id: Int,
    @DrawableRes star6Id: Int,
) {
    Row(
        Modifier
            .padding(start = padding, bottom = padding)
            .wrapContentSize()
            .align(Alignment.BottomStart)
            .layerAlpha(1 - layerAlpha.value, hasUnique1)
    ) {
        repeat(maxRarity) { r ->
            val id = if (r < rarity) {
                if (r > 4) {
                    star6Id
                } else {
                    star1Id
                }
            } else {
                star0Id
            }
            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier
                    .size(starSize)
                    .offsetX((-2.5 * r).dp, isIcon)
            )
        }
    }
}

@Composable
private fun BoxScope.Position(
    padding: Dp,
    layerAlpha: State<Float>,
    enablePositionAlpha: Boolean,
    position: Int,
    positionSize: Dp
) {
    Box(
        Modifier
            .padding(end = padding, bottom = padding)
            .wrapContentSize()
            .align(Alignment.BottomEnd)
            .layerAlpha(layerAlpha.value, enablePositionAlpha)
    ) {
        val id = when (position) {
            1 -> R.drawable.position_1
            2 -> R.drawable.position_2
            else -> R.drawable.position_3
        }
        Image(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.size(positionSize)
        )
    }
}

@Composable
private fun BoxScope.Unique(
    padding: Dp,
    layerAlpha: State<Float>,
    uniqueSize: Dp,
    @DrawableRes uniqueId: Int
) {
    Box(
        Modifier
            .padding(start = padding, bottom = padding)
            .wrapContentSize()
            .align(Alignment.BottomStart)
            .layerAlpha(layerAlpha.value, true)
    ) {
        Image(
            painter = painterResource(uniqueId),
            contentDescription = null,
            modifier = Modifier.size(uniqueSize)
        )
    }
}


private fun Modifier.offsetX(value: Dp, enable: Boolean): Modifier {
    return if (enable) {
        this.offset(value, 0.dp)
    } else {
        this
    }
}

private fun Modifier.layerAlpha(value: Float, enable: Boolean): Modifier {
    return if (enable) {
        this.graphicsLayer { alpha = value }
    } else {
        this
    }
}
