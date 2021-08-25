package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.theme.ImmersiveSysUi

@Composable
fun ImmersiveBackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onBack, modifier) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .background(ImmersiveSysUi, CircleShape)
                .padding(4.dp),
            tint = Color.White
        )
    }
}
