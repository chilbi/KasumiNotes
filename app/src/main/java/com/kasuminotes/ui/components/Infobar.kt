package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Infobar(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    width: Dp = 72.dp,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    style: UnderlineStyle = UnderlineStyle.Dashed,
    fontSize: TextUnit = 12.sp,
    textAlign: TextAlign? = null
) {
    InfobarImpl(
        label = label,
        stringValue = value,
        annotatedStringValue = null,
        modifier = modifier,
        width = width,
        color = color,
        contentColor = contentColor,
        style = style,
        fontSize = fontSize,
        textAlign = textAlign
    )
}

@Composable
fun Infobar(
    label: String,
    value: AnnotatedString,
    modifier: Modifier = Modifier,
    width: Dp = 72.dp,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    style: UnderlineStyle = UnderlineStyle.Dashed,
    fontSize: TextUnit = 12.sp,
    textAlign: TextAlign? = null
) {
    InfobarImpl(
        label = label,
        stringValue = null,
        annotatedStringValue = value,
        modifier = modifier,
        width = width,
        color = color,
        contentColor = contentColor,
        style = style,
        fontSize = fontSize,
        textAlign = textAlign
    )
}

@Composable
private fun InfobarImpl(
    label: String,
    stringValue: String?,
    annotatedStringValue: AnnotatedString?,
    modifier: Modifier = Modifier,
    width: Dp,
    color: Color,
    contentColor: Color,
    style: UnderlineStyle,
    fontSize: TextUnit,
    textAlign: TextAlign?
) {
    Row(
        modifier
            .padding(4.dp)
            .underline(
                width = 1.dp,
                color = color,
                style = style,
                startPointX = width - 4.dp
            )
    ) {
        Text(
            text = label,
            modifier = Modifier
                .width(width)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 4.dp, vertical = 2.dp),
            color = contentColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = textAlign
        )

        when {
            stringValue != null ->
                Text(
                    text = stringValue,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    fontSize = fontSize,
                    textAlign = TextAlign.End
                )
            annotatedStringValue != null ->
                Text(
                    text = annotatedStringValue,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                    fontSize = fontSize,
                    textAlign = TextAlign.End
                )
        }
    }
}
