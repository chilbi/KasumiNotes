package com.kasuminotes.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BadgedButton(
    value: Int,
    originValue: Int,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 32.dp),
                role = Role.Button,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        label()

        BadgedDiffBox(value, originValue) {
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun BadgedButtonDialog(
    value: Int,
    originValue: Int,
    label: @Composable () -> Unit,
    content: @Composable (onClose: () -> Unit) -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val onOpen = { visible = true }
    val onClose = { visible = false }

    BadgedButton(value, originValue, onOpen, label)

    if (visible) {
        Dialog(onClose) {
            content(onClose)
        }
    }
}

@Composable
fun LabelImage(@DrawableRes id: Int) {
    Image(
        painter = painterResource(id),
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun LabelText(
    text: String,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(color),
) {
    Text(
        text =text,
        modifier = Modifier
            .width(40.dp)
            .background(
                color = color,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(vertical = 2.dp),
        color = contentColor,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall
    )
}
