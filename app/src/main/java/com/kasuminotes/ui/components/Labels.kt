package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.ui.theme.Diamond

@Composable
fun AdaptiveWidthLabel(
    text: String,
    margin: PaddingValues = PaddingValues(4.dp),
    padding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(margin)
            .background(color, shape)
            .padding(padding),
        color = contentColor,
        style = style
    )
}

@Composable
fun FixedWidthLabel(
    text: String,
    width: Dp = 72.dp,
    margin: PaddingValues = PaddingValues(4.dp),
    padding: PaddingValues = PaddingValues(vertical = 2.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(margin)
            .width(width)
            .background(color, shape)
            .padding(padding),
        color = contentColor,
        textAlign = TextAlign.Center,
        style = style
    )
}

@Composable
fun UnderlineLabel(
    label: String,
    color: Color,
    thickness: Dp = 1f.dp,
    style: UnderlineStyle = UnderlineStyle.Solid,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .underline(thickness, color, style)
            .padding(bottom = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.diamond_s, label),
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ActionLabel(actionNum: Int) {
    Box(
        Modifier
            .size(20.dp)
            .background(MaterialTheme.colorScheme.primary, Diamond),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = actionNum.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
