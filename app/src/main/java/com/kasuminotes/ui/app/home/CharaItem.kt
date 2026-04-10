package com.kasuminotes.ui.app.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.common.AtkType
import com.kasuminotes.common.OrderBy
import com.kasuminotes.common.Position
import com.kasuminotes.common.Role
import com.kasuminotes.common.Talent
import com.kasuminotes.data.UserProfile
import com.kasuminotes.state.CharaImageState
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.SizedBox
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
//        val maxRarity = userProfile.unitData.maxRarity
        val rarity = userProfile.userData.rarity
        val hasUnique1 = userProfile.unitData.hasUnique1 && userProfile.userData.unique1Level > 0
        val hasUnique2 = userProfile.unitData.hasUnique2 && userProfile.userData.unique2Level > -1
//        val enablePositionAlpha = charaImageState.isIcon && maxRarity > 5

        RarityBorderShadow()

        CharaImage(
            charaImageState.isPlate,
            userProfile.unitData.unitId,
            rarity,
            charaImageState.getImageUrl,
            onClick = { onCharaClick(userProfile) }
        )

        PromotionFrame(userProfile.userData.rankRarity)

//        Position(
//            padding,
//            layerAlpha,
//            enablePositionAlpha,
//            userProfile.unitData.position,
//            charaImageState.positionSize
//        )
        AtkTypeAndPosition(
            padding,
            userProfile.unitData.positionId,
            userProfile.unitData.atkType,
            charaImageState.positionSize
        )

//        Column(Modifier.align(Alignment.TopStart).padding(start = padding, top = padding)) {
//            UnitTalent(
//                0.dp,
//                userProfile.unitData.talentId,
//                charaImageState.positionSize
//            )
//            Spacer(Modifier.size(padding))
//            UnitRole(
//                0.dp,
//                userProfile.unitData.unitRoleId,
//                charaImageState.positionSize
//            )
//        }
        RoleAndTalent(
//            padding,
            userProfile.unitData.unitRoleId,
            userProfile.unitData.talentId,
            charaImageState.isIcon
        )

        Rarity(
            padding,
            layerAlpha,
            hasUnique1 || hasUnique2,
            rarity,
            charaImageState.isIcon
        )
//        Rarities(
//            padding,
//            layerAlpha,
//            hasUnique1 || hasUnique2,
//            maxRarity,
//            rarity,
//            charaImageState.starSize,
//            charaImageState.isIcon,
//            charaImageState.star0Id,
//            charaImageState.star1Id,
//            charaImageState.star6Id
//        )

        if (hasUnique1 || hasUnique2) {
            val uniqueId = if (hasUnique2) charaImageState.unique2Id else charaImageState.unique1Id
            Unique(
                padding,
                layerAlpha,
                charaImageState.uniqueSize,
                uniqueId
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

val rarityBorderMap = mutableMapOf<Int, Modifier>()

@Composable
private fun PromotionFrame(rankRarity: Int) {
    val rarityBorder = rarityBorderMap.getOrPut(rankRarity) {
        Modifier.rarityBorder(rankRarity)
    }
    Box(modifier = rarityBorder)
}

@Composable
private fun BoxScope.AtkTypeAndPosition(
    padding: Dp,
    positionId: Int,
    atkType: Int,
    size: Dp
) {
    Column(
        Modifier
            .align(Alignment.TopStart)
            .padding(start = padding, top = padding)
    ) {
        Image(
            painter = painterResource(AtkType.fromId(atkType).imgId),
            contentDescription = null,
            modifier = Modifier.size(size)
        )
        Image(
            painter = painterResource(Position.fromId(positionId).imgId),
            contentDescription = null,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
private fun BoxScope.RoleAndTalent(
//    padding: Dp,
    roleId: Int,
    talentId: Int,
    isIcon: Boolean
) {
    Row(
        Modifier
            .align(Alignment.BottomEnd)
//            .padding(end = padding, bottom = padding)
    ) {
        val size = if (isIcon) 16.dp else 20.dp
        Image(
            painter = painterResource(Role.fromId(roleId).halfImgId),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(size)
        )
        Image(
            painter = painterResource(Talent.fromId(talentId).halfImgId),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(size)
        )
    }
}

@Composable
private fun BoxScope.Rarity(
    padding: Dp,
    layerAlpha: State<Float>,
    hasUnique: Boolean,
    rarity: Int,
    isIcon: Boolean
) {
    Box(
        Modifier
            .align(Alignment.BottomStart)
            .padding(start = padding, bottom = padding)
            .layerAlpha(1 - layerAlpha.value, hasUnique)
    ) {
        val id = when (rarity) {
            1 -> R.drawable.common_unit_icon_star_1
            2 -> R.drawable.common_unit_icon_star_2
            3 -> R.drawable.common_unit_icon_star_down_3
            4 -> R.drawable.common_unit_icon_star_down_4
            6 -> R.drawable.common_unit_icon_star_6
            else -> R.drawable.common_unit_icon_star_5
        }
        val size = if (isIcon) 18.dp else 22.dp
        Image(
            painter = painterResource(id),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(size)
        )
    }
}

//@Composable
//private fun BoxScope.Rarities(
//    padding: Dp,
//    layerAlpha: State<Float>,
//    hasUnique: Boolean,
//    maxRarity: Int,
//    rarity: Int,
//    starSize: Dp,
//    isIcon: Boolean,
//    @DrawableRes star0Id: Int,
//    @DrawableRes star1Id: Int,
//    @DrawableRes star6Id: Int
//) {
//    Row(
//        Modifier
//            .align(Alignment.BottomStart)
//            .padding(start = padding, bottom = padding)
//            .layerAlpha(1 - layerAlpha.value, hasUnique)
//    ) {
//        repeat(maxRarity) { r ->
//            val id = if (r < rarity) {
//                if (r > 4) {
//                    star6Id
//                } else {
//                    star1Id
//                }
//            } else {
//                star0Id
//            }
//            Image(
//                painter = painterResource(id),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(starSize)
//                    .offsetX((-2 * r).dp, isIcon)
//            )
//        }
//    }
//}

//@Composable
//private fun BoxScope.Position(
//    padding: Dp,
//    layerAlpha: State<Float>,
//    enablePositionAlpha: Boolean,
//    positionId: Int,
//    positionSize: Dp
//) {
//    Box(
//        Modifier
//            .align(Alignment.TopStart)
//            .padding(end = padding, bottom = padding)
//            .layerAlpha(layerAlpha.value, enablePositionAlpha)
//    ) {
//        Image(
//            painter = painterResource(Position.fromId(positionId).imgId),
//            contentDescription = null,
//            modifier = Modifier.size(positionSize)
//        )
//    }
//}

@Composable
private fun BoxScope.Unique(
    padding: Dp,
    layerAlpha: State<Float>,
    uniqueSize: Dp,
    @DrawableRes uniqueId: Int
) {
    Box(
        Modifier
            .align(Alignment.BottomStart)
            .padding(start = padding, bottom = padding)
            .layerAlpha(layerAlpha.value, true)
    ) {
        Image(
            painter = painterResource(uniqueId),
            contentDescription = null,
            modifier = Modifier.size(uniqueSize)
        )
    }
}


//private fun Modifier.offsetX(value: Dp, enable: Boolean): Modifier {
//    return if (enable) {
//        this.offset(value, 0.dp)
//    } else {
//        this
//    }
//}

private fun Modifier.layerAlpha(value: Float, enable: Boolean): Modifier {
    return if (enable) {
        this.graphicsLayer { alpha = value }
    } else {
        this
    }
}
