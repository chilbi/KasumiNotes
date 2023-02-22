package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImageCard
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.utils.UrlUtil

@Composable
fun EquipTopBar(
    equipmentId: Int,
    equipmentName: String,
    equipmentType: String,
    onBack: () -> Unit
) {
    ImmersiveTopAppBar(
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
