package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.kasuminotes.ui.components.ImmersiveBackButton
import com.kasuminotes.ui.components.StillBox
import com.kasuminotes.ui.theme.ImmersiveSysUi

@ExperimentalCoilApi
@Composable
fun CharaTopBar(
    unitId: Int,
    rarity: Int,
    unitName: String,
    statusBarHeight: Dp,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        StillBox(
            unitId,
            rarity,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = statusBarHeight)
                    .background(ImmersiveSysUi)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ImmersiveBackButton(onBack)

                    Text(
                        text = unitName,
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
