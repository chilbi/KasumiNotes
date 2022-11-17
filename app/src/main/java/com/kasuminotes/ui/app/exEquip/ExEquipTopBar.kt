package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.components.BackButton
import com.kasuminotes.ui.components.ImmersiveTopAppBar
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipTopBar(
    category: Int,
    categoryName: String,
    onBack: () -> Unit
) {
    ImmersiveTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(32.dp)) {
                    PlaceImage(UrlUtil.getExEquipCategoryUrl(category))
                }
                Text(
                    text = categoryName,
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 16.sp
                )
            }
        },
        navigationIcon = {
            BackButton(onBack)
        }
    )
}
