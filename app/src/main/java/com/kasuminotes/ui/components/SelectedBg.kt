package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

fun Modifier.selectedBg(selected: Boolean, color: Color, shape: Shape): Modifier {
    return if (selected) this.background(color, shape) else this
}
