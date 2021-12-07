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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

        DiffBadgedBox(value, originValue) {
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
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
    background: Color = MaterialTheme.colors.primary,
    color: Color = MaterialTheme.colors.onPrimary,
) {
    Text(
        text =text,
        modifier = Modifier
            .width(40.dp)
            .background(
                color = background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 2.dp),
        color = color,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}
