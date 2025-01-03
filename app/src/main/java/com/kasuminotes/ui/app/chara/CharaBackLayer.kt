package com.kasuminotes.ui.app.chara

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import com.kasuminotes.R
import com.kasuminotes.data.EquipData
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.data.Property
import com.kasuminotes.data.UniqueData
import com.kasuminotes.data.UnitData
import com.kasuminotes.data.ExEquipSlot
import com.kasuminotes.data.UnitPromotion
import com.kasuminotes.data.UserData
import com.kasuminotes.ui.app.DefaultUserId
import com.kasuminotes.ui.components.Alert
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.ImageSize
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.TranslucentBackButton
import com.kasuminotes.ui.components.Severity
import com.kasuminotes.ui.components.StillBox
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.theme.DarkError
import com.kasuminotes.ui.theme.DarkInfo
import com.kasuminotes.ui.theme.DarkWarning
import com.kasuminotes.ui.theme.Translucent
import com.kasuminotes.utils.UrlUtil

@Composable
fun CharaBackLayer(
    userData: UserData,
    unitData: UnitData,
    maxUserData: MaxUserData,
    unitPromotion: UnitPromotion?,
    unique1Data: UniqueData?,
    unique2Data: UniqueData?,
    exEquipSlots: List<ExEquipSlot>,
    rankBonusProperty: Property?,
    property: Property,
    originProperty: Property,
    originUserData: UserData,
    saveVisible: Boolean,
    statusBarHeight: Dp,
    headerHeight: Dp,
    onBack: () -> Unit,
    onEquipClick: (equipData: EquipData, slot: Int) -> Unit,
    onUniqueClick: (uniqueData: UniqueData, slot: Int) -> Unit,
    onExEquipClick: (ExEquipSlot) -> Unit,
    onEquipChange: (equip: Boolean, slot: Int) -> Unit,
    onUniqueChange: (equip: Boolean, slot: Int) -> Unit,
    onCharaLevelChange: (Int) -> Unit,
    onRarityChange: (Int) -> Unit,
    onUniqueLevelChange: (value: Int, slot: Int) -> Unit,
    onLoveLevelChange: (Int) -> Unit,
    onPromotionLevelChange: (Int) -> Unit,
    onSkillLevelChange: (value: Int, labelText: String) -> Unit,
    onLvLimitBreakChange: (maxCharaLevel: Int) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        CharaHeader(
            unitData.unitId,
            userData.rarity,
            unitData.unitName,
            statusBarHeight,
            onBack
        )

        VerticalGrid(
            size = 2,
            cells = VerticalGridCells.Adaptive(400.dp)
        ) { index ->
            when (index) {
                0 -> {
                    CharaUserData(
                        userData,
                        originUserData,
                        maxUserData,
                        unitData.maxRarity,
                        unitData.hasUnique1,
                        unitPromotion,
                        unique1Data,
                        unique2Data,
                        exEquipSlots,
                        onEquipClick,
                        onUniqueClick,
                        onExEquipClick,
                        onEquipChange,
                        onUniqueChange,
                        onCharaLevelChange,
                        onRarityChange,
                        onUniqueLevelChange,
                        onLoveLevelChange,
                        onPromotionLevelChange,
                        onSkillLevelChange,
                        onLvLimitBreakChange,
                    )
                }
                1 -> {
                    Container(margin = 8.dp) {
                        Box {
                            PropertyTable(
                                property = property,
                                originProperty = originProperty,
                            )
                            RankBonusButton(rankBonusProperty)
                        }
                    }
                }
            }
        }

        AlertMessage(userData.userId, saveVisible, onCancel, onSave)

        Spacer(Modifier.height(headerHeight))
    }
}

@Composable
private fun BoxScope.RankBonusButton(rankBonusProperty: Property?) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val onOpen = remember {{ visible = true }}
    val onClose = remember {{ visible = false }}
    val label = "${stringResource(R.string.rank)} ${stringResource(R.string.bonus)}"
    val hasBonus = rankBonusProperty != null

    TextButton(
        onClick = onOpen,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(0.dp, 8.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
        enabled = hasBonus
    ) {
        Icon(if (hasBonus) Icons.Filled.Check else Icons.Filled.Close, null)
        Text(
            text = label,
            textDecoration = TextDecoration.Underline
        )
    }

    if (visible && hasBonus) {
        Dialog(onClose) {
            LabelContainer(
                label = label,
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                PropertyTable(
                    property = rankBonusProperty!!,
                    indices = rankBonusProperty.nonzeroIndices
                )
            }
        }
    }
}

@Composable
private fun CharaHeader(
    unitId: Int,
    rarity: Int,
    unitName: String,
    statusBarHeight: Dp,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(UrlUtil.getUnitStillUrl(unitId, rarity))
                    .transformations(BlurTransformation(LocalContext.current, 20f, 1.5f))
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillWidth
        )

        StillBox(
            unitId,
            rarity,
            Modifier
                .sizeIn(maxWidth = 420.dp)
                .then(ImageSize.StillModifier)
        ) {
            TranslucentBackButton(
                onBack,
                Modifier.padding(top = statusBarHeight)
            )

            CharaName(
                unitName,
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(36.dp)
            )
        }
    }
}

@Composable
private fun AlertMessage(
    userId: Int,
    saveVisible: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    var errorVisible by rememberSaveable { mutableStateOf(true) }

    var infoVisible by rememberSaveable { mutableStateOf(true) }

    AnimatedVisibility(visible = saveVisible) {
        Alert(
            severity = Severity.Warning,
            text = stringResource(R.string.save_to_database),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            padding = PaddingValues(vertical = 4.dp)
        ) {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.DarkGray)
            ) {
                Text(stringResource(R.string.cancel))
            }

            TextButton(
                onClick = onSave,
                colors = ButtonDefaults.textButtonColors(contentColor = DarkWarning)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    AnimatedVisibility(
        visible = errorVisible && userId == DefaultUserId && saveVisible
    ) {
        Alert(
            severity = Severity.Error,
            text = stringResource(R.string.default_user_warning),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(
                onClick = { errorVisible = false },
                colors = IconButtonDefaults.iconButtonColors(contentColor = DarkError)
            ) {
                Icon(Icons.Filled.Close, null)
            }
        }
    }

    AnimatedVisibility(visible = infoVisible) {
        Alert(
            severity = Severity.Info,
            text = stringResource(R.string.swipe_equip_info),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(
                onClick = { infoVisible = false },
                colors = IconButtonDefaults.iconButtonColors(contentColor = DarkInfo)
            ) {
                Icon(Icons.Filled.Close, null)
            }
        }
    }
}

@Composable
private fun CharaName(
    unitName: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Box(
            Modifier
                .fillMaxHeight()
                .background(Translucent)
        ) {
            Text(
                text = unitName,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        0.0f to Translucent,
                        0.2f to Color.Transparent
                    )
                )
        )
    }
}
