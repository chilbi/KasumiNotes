package com.kasuminotes.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kasuminotes.utils.UrlUtil

@Composable
fun Container(
    modifier: Modifier = Modifier,
    margin: Dp = 4.dp,
    padding: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    color: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 3.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick == null) {
        Surface(
            modifier = modifier.padding(margin),
            shape = shape,
            color = color,
            tonalElevation = tonalElevation
        ) {
            Column(modifier = Modifier.padding(padding), content = content)
        }
    } else {
        Surface(
            onClick = onClick,
            modifier = modifier.padding(margin),
            shape = shape,
            color = color,
            tonalElevation = tonalElevation
        ) {
            Column(modifier = Modifier.padding(padding), content = content)
        }
    }
}

@Composable
fun LabelContainer(
    label: String,
    color: Color,
    thickness: Dp = 1f.dp,
    style: UnderlineStyle = UnderlineStyle.Solid,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier,
    margin: Dp = 4.dp,
    padding: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 3.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Container(modifier, margin, padding, shape, containerColor, tonalElevation, onClick) {
        UnderlineLabel(label, color, thickness, style)
        content()
    }
}

@Composable
fun SizedBox(
    ratio: Float,
    isIcon: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(Modifier.imageSize(ratio)) {
        if (isIcon && maxWidth > 84.dp) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .align(Alignment.Center),
                content = content
            )
        } else {
            Box(
                modifier = Modifier.padding(4.dp),
                content = content
            )
        }
    }
}

@Composable
fun StillBox(
    unitId: Int,
    rarity: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier) {
        PlaceImage(
            url = UrlUtil.getUnitStillUrl(unitId, rarity),
            shape = RectangleShape
        )

        content()
    }
}
