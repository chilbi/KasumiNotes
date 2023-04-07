package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.components.TranslucentBackButton
import com.kasuminotes.ui.components.StillBox
import com.kasuminotes.ui.theme.Translucent

@Composable
fun CharaTopBar(
    unitId: Int,
    rarity: Int,
    unitName: String,
    statusBarHeight: Dp,
    onBack: () -> Unit
) {
    StillBox(
        unitId,
        rarity,
        Modifier.fillMaxWidth().height(200.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = statusBarHeight)
                .background(Translucent)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TranslucentBackButton(onBack)

                Text(
                    text = unitName,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
