package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.ui.theme.Rounded8

@Composable
fun UnderlineLabelColumn(
    label: String,
    color: Color,
    width: Dp = 1f.dp,
    style: UnderlineStyle = UnderlineStyle.Solid,
    shape: Shape = Rounded8,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    BgBorderColumn(
        shape = shape,
        padding = 8.dp,
        onClick = onClick
    ) {
        UnderlineLabel(label, color, width, style)

        content()
    }
}

@Composable
fun UnderlineLabel(
    label: String,
    color: Color,
    width: Dp = 1f.dp,
    style: UnderlineStyle = UnderlineStyle.Solid,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .underline(width, color, style)
            .padding(bottom = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.diamond_s, label),
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
