package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.state.ExEquipState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquip(
    exEquipState: ExEquipState,
    onBack: () -> Unit
) {
    val exEquipCategory = exEquipState.exEquipCategory
    val exEquipData = exEquipState.exEquipData

    Scaffold(
        topBar = {
            if (exEquipCategory != null) {
                ExEquipTopBar(exEquipCategory.category, exEquipCategory.categoryName, onBack)
            }
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        floatingActionButton = {
            if (exEquipState.exEquipData != null) {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(if (exEquipState.isEquipping) R.string.demount_equip else R.string.equipping)) },
                    icon = { Icon(if (exEquipState.isEquipping) Icons.Filled.Clear else Icons.Filled.Check, null) },
                    onClick = exEquipState::changeExEquip
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                EquippableExList(
                    exEquipState.equippableExList,
                    exEquipData?.exEquipmentId ?: 0,
                    exEquipState.exEquipSlot?.exEquipData?.exEquipmentId ?: 0,
                    exEquipState::initExEquip
                )

                if (exEquipData != null) {
                    ExEquipInfo(
                        exEquipData.exEquipmentId,
                        exEquipData.name,
                        exEquipData.description
                    )

                    ExEquipProperty(
                        exEquipState.percentProperty,
                        exEquipData.rarity,
                        exEquipState.maxEnhanceLevel,
                        exEquipState.enhanceLevel,
                        exEquipState::valueDisplay,
                        exEquipState::changeEnhanceLevel
                    )

                    if (!exEquipData.subStatusList.isNullOrEmpty()) {
                        ExEquipSubProperty(
                            exEquipData.subStatusList,
                            exEquipState.subPercentList,
                            exEquipState::valueDisplay,
                            exEquipState::changeSubPercentList,
                            exEquipState::changeSubPercent,
                            exEquipState::changeSubPercentValue
                        )
                    }

                    if (exEquipData.passiveSkill1 != null || exEquipData.passiveSkill2 != null) {
                        ExEquipSkill(
                            exEquipData.passiveSkill1,
                            exEquipData.passiveSkill2,
                            exEquipState.battleProperty
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun ExEquipTopBar(
    category: Int,
    categoryName: String,
    onBack: () -> Unit
) {
    TopBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.padding(end = 8.dp).size(24.dp)) {
                    PlaceImage(UrlUtil.getExEquipCategoryUrl(category))
                }
                Text(categoryName)
            }
        },
        navigationIcon = {
            BackButton(onBack)
        }
    )
}
