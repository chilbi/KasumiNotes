package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasuminotes.data.Property
import com.kasuminotes.state.DbState
import com.kasuminotes.state.EquipState
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.TopBar
import com.kasuminotes.ui.components.MultiLineText
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.utils.UrlUtil

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
        containerColor = MaterialTheme.colorScheme.surface,
        content = { contentPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                VerticalGrid(
                    size = 2,
                    cells = VerticalGridCells.Adaptive(400.dp)
                ) { index ->
                    when (index) {
                        0 -> {
                            Container {
                                MultiLineText(description)
                            }
                        }
                        1 -> {
                            EquipProperty(
                                equipState.enhanceLevel,
                                equipState.maxEnhanceLevel,
                                baseProperty,
                                equipState.property,
                                equipState::changeEnhanceLevel
                            )
                        }
                    }
                }

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

@Composable
private fun EquipTopBar(
    equipmentId: Int,
    equipmentName: String,
    equipmentType: String,
    onBack: () -> Unit
) {
    TopBar(
        title = {
            ImageCard(
                imageUrl = UrlUtil.getEquipIconUrl(equipmentId),
                primaryText = equipmentName,
                secondaryText = equipmentType,
                paddingValues = PaddingValues(vertical = 4.dp)
            )
        },
        navigationIcon = {
            BackButton(onBack)
        }
    )
}
