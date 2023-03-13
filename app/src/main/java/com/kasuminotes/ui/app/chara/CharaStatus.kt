package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.components.BadgedButtonDialog
import com.kasuminotes.ui.components.LabelImage
import com.kasuminotes.ui.components.LabelText
import com.kasuminotes.ui.components.Rarities
import com.kasuminotes.ui.components.SliderPlus
import com.kasuminotes.ui.theme.RaritiesColors
import com.kasuminotes.ui.theme.rankRarity

@Composable
fun CharaStatus(
    userData: UserData,
    originUserData: UserData,
    maxUserData: MaxUserData,
    maxRarity: Int,
    hasUnique: Boolean,
    modifier: Modifier = Modifier,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (Int) -> Unit,
    onLoveLevelChange: (Int) -> Unit,
    onPromotionLevelChange: (Int) -> Unit,
    onSkillLevelChange: (value: Int, labelText: String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BadgedButtonDialog(
            userData.charaLevel,
            originUserData.charaLevel,
            label = { LabelText(stringResource(R.string.level)) }
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
                    SliderPlus(
                        value = userData.charaLevel,
                        minValue = 1,
                        maxValue = maxUserData.maxCharaLevel + userData.lvLimitBreak,
                        onValueChange = onCharaLevelChange
                    ) {
                        LabelText(stringResource(R.string.level))
                    }
                    SliderPlus(
                        value = userData.ubLevel,
                        minValue = 1,
                        maxValue = userData.charaLevel,
                        onValueChange = { onSkillLevelChange(it, "UB") }
                    ) {
                        LabelText("UB")
                    }
                    SliderPlus(
                        value = userData.skill1Level,
                        minValue = 1,
                        maxValue = userData.charaLevel,
                        onValueChange = { onSkillLevelChange(it, "Main 1") }
                    ) {
                        LabelText("Main 1")
                    }
                    SliderPlus(
                        value = userData.skill2Level,
                        minValue = 1,
                        maxValue = userData.charaLevel,
                        onValueChange = { onSkillLevelChange(it, "Main 2") }
                    ) {
                        LabelText("Main 2")
                    }
                    SliderPlus(
                        value = userData.exLevel,
                        minValue = 1,
                        maxValue = userData.charaLevel,
                        onValueChange = { onSkillLevelChange(it, "EX") }
                    ) {
                        LabelText("EX")
                    }
                }
            }
        }

        BadgedButtonDialog(
            userData.rarity,
            originUserData.rarity,
            label = {
                LabelImage(
                    if (userData.rarity > 5) R.drawable.star_large_6
                    else R.drawable.star_large_1
                )
            }
        ) { onClose ->
            Surface(
                shape = CircleShape,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Rarities(
                    highlightCount = if (maxRarity > 5) 1 else 0,
                    maxRarity = maxRarity,
                    rarity = userData.rarity,
                    onRarityChange = {
                        onClose()
                        onRarityChange(it)
                    },
                    modifier = Modifier.padding(24.dp),
                    size = 28.dp,
                    padding = PaddingValues(8.dp)
                )
            }
        }

        if (hasUnique) {
            BadgedButtonDialog(
                userData.uniqueLevel,
                originUserData.uniqueLevel,
                label = { LabelImage(R.drawable.unique_large) }
            ) {
                Surface(
                    shape = CircleShape,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Box(Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
                        SliderPlus(
                            value = userData.uniqueLevel,
                            minValue = 0,
                            maxValue = maxUserData.maxUniqueLevel,
                            onValueChange = onUniqueLevelChange,
                        ) {
                            LabelImage(R.drawable.unique_large)
                        }
                    }
                }
            }
        }

        BadgedButtonDialog(
            userData.loveLevel,
            originUserData.loveLevel,
            label = { LabelImage(R.drawable.love_level) }
        ) {
            Surface(
                shape = CircleShape,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Box(Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
                    SliderPlus(
                        value = userData.loveLevel,
                        minValue = 0,
                        maxValue = if (maxRarity > 5) 12 else 8,
                        onValueChange = onLoveLevelChange
                    ) {
                        LabelImage(R.drawable.love_level)
                    }
                }
            }
        }

        BadgedButtonDialog(
            userData.promotionLevel,
            originUserData.promotionLevel,
            label = {
                LabelText(
                    text = stringResource(R.string.rank),
                    color = RaritiesColors.getRarityColors(userData.rankRarity).middle,
                    contentColor = Color.White
                )
            }
        ) { onClose ->
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    Modifier
                        .height(320.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
//                    val bgColor = if (MaterialTheme.colors.isLight) {
//                        Color(0xFFF5F5F5)
//                    } else {
//                        Color.White
//                    }
                    var i = maxUserData.maxPromotionLevel
                    while (i > 0) {
                        RankCapsule(
                            promotionLevel = i,
                            onClick = {
                                onClose()
                                onPromotionLevelChange(it)
                            }
                        )
                        i--
                    }
                }
            }
        }
    }
}

@Composable
private fun RankCapsule(
    promotionLevel: Int,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    onClick: (Int) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(Color.White)
            .clickable(role = Role.RadioButton) {
                onClick(promotionLevel)
            }
    ) {
        Text(
            text = stringResource(R.string.rank),
            modifier = Modifier
                .background(RaritiesColors.getRarityColors(promotionLevel.rankRarity).middle)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            style = style
        )

        Text(
            text = promotionLevel.toString(),
            modifier = Modifier
                .width(32.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.Black,
            textAlign = TextAlign.End,
            style = style
        )
    }
}
