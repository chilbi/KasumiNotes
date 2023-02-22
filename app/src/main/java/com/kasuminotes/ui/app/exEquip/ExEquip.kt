package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.app.state.ExEquipState

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
                FloatingActionButton(onClick = { exEquipState.changeExEquip() }) {
                    Icon(if (exEquipState.isEquipping) Icons.Filled.Clear else Icons.Filled.Check, null)
                }
            }
        },
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
                        exEquipState.baseProperty,
                        exEquipState.maxEnhanceLevel,
                        exEquipState.enhanceLevel,
                        exEquipState::changeEnhanceLevel
                    )

                    if (exEquipData.passiveSkill1 != null || exEquipData.passiveSkill2 != null) {
                        ExEquipSkill(
                            exEquipData.passiveSkill1,
                            exEquipData.passiveSkill2,
                            exEquipState.baseProperty
                        )
                    }
                }
            }
        }
    )
}
