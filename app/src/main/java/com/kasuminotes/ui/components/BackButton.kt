package com.kasuminotes.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onBack) {
        Icon(Icons.Filled.ArrowBack, null)
    }
}
