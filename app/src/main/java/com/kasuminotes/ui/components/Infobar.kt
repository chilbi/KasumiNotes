package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Infobar(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    underlineStyle: UnderlineStyle = UnderlineStyle.Dashed,
    width: Dp = 72.dp,
    margin: PaddingValues = PaddingValues(4.dp),
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    textAlign: TextAlign? = null,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    InfobarImpl(
        label = label,
        stringValue = value,
        annotatedStringValue = null,
        modifier = modifier,
        underlineStyle = underlineStyle,
        width = width,
        margin = margin,
        padding = padding,
        color = color,
        contentColor = contentColor,
        shape = shape,
        textAlign = textAlign,
        labelStyle = labelStyle,
        valueStyle = valueStyle
    )
}

@Composable
fun Infobar(
    label: String,
    value: AnnotatedString,
    modifier: Modifier = Modifier,
    underlineStyle: UnderlineStyle = UnderlineStyle.Dashed,
    width: Dp = 72.dp,
    margin: PaddingValues = PaddingValues(4.dp),
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    textAlign: TextAlign? = null,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    InfobarImpl(
        label = label,
        stringValue = null,
        annotatedStringValue = value,
        modifier = modifier,
        underlineStyle = underlineStyle,
        width = width,
        margin = margin,
        padding = padding,
        color = color,
        contentColor = contentColor,
        shape = shape,
        textAlign = textAlign,
        labelStyle = labelStyle,
        valueStyle = valueStyle
    )
}

@Composable
private fun InfobarImpl(
    label: String,
    stringValue: String?,
    annotatedStringValue: AnnotatedString?,
    modifier: Modifier = Modifier,
    underlineStyle: UnderlineStyle = UnderlineStyle.Dashed,
    width: Dp = 72.dp,
    margin: PaddingValues = PaddingValues(4.dp),
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    textAlign: TextAlign? = null,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    Row(
        modifier
            .padding(margin)
            .underline(1.dp, color, underlineStyle, width - 8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .width(width)
                .background(color, shape)
                .padding(padding),
            color = contentColor,
            textAlign = textAlign,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
            style = labelStyle
        )

        when {
            stringValue != null ->
                Text(
                    text = stringValue,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding()),
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    maxLines = 1,
                    style = valueStyle
                )
            annotatedStringValue != null ->
                Text(
                    text = annotatedStringValue,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding()),
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    maxLines = 1,
                    style = valueStyle
                )
        }
    }
}
