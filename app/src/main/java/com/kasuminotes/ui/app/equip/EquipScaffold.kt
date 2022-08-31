package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.Property
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.EquipState
import com.kasuminotes.ui.components.BgBorderColumn
import com.kasuminotes.ui.components.MultiLineText

@Composable
fun EquipScaffold(
    dbState: DbState,
    equipState: EquipState,
    equipmentId: Int,
    equipmentName: String,
    equipmentType: String,
    description: String,
    baseProperty: Property,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            EquipTopBar(
                equipmentId,
                equipmentName,
                equipmentType,
                onBack
            )
        },
        bottomBar = {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        },
        content = { contentPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                BgBorderColumn {
                    MultiLineText(description)
                }

                EquipProperty(
                    equipState.enhanceLevel,
                    equipState.maxEnhanceLevel,
                    baseProperty,
                    equipState.property,
                    equipState::changeEnhanceLevel
                )

                if (equipState.equipCraftList != null) {
                    EquipCraftList(
                        equipState.equipCraftList!!,
                        equipState.searchList ?: emptyList(),
                        !dbState.questInitializing,
                        equipState::changeSearchList
                    )
                }

                if (equipState.uniqueCraftList != null) {
                    UniqueCraftList(
                        equipState.uniqueCraftList!!,
                        equipState::changeEnhanceLevel,
                        Modifier.weight(1f)
                    )
                }

                if (equipState.searchList != null) {
                    EquipDropList(
                        equipState.questTypes,
                        equipState.questDataList,
                        equipState.searchList!!,
                        equipState.sortDesc,
                        dbState.userState.maxUserData!!.maxArea > 36,
                        equipState.min37,
                        equipState::changeQuestTypes,
                        equipState::toggleSortDesc,
                        equipState::toggleMin37
                    )
                }
            }
        }
    )
}
