package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kasuminotes.ui.theme.ImmersiveSysUi

@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onBack) {
        Icon(Icons.Filled.ArrowBack, null)
    }
}

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

@Composable
fun SortIconButton(
    sortDesc: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = Icons.Filled.Sort,
            contentDescription = null,
            modifier = Modifier.rotate(if (sortDesc) 0f else 180f)
        )
    }
}

@Composable
fun Toggle37Button(
    checked: Boolean,
    onToggle: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface,
    checkedColor: Color = color.copy(0.35f)
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = { onToggle()  }
    ) {
        Text(
            text = "37↑",
            color = if (checked) color else checkedColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TextToggleButton(
    leftText: String,
    rightText: String,
    leftChecked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.tertiary,
    contentColor: Color = contentColorFor(color),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    padding: PaddingValues = PaddingValues(4.dp),
    style: TextStyle = MaterialTheme.typography.labelLarge
) {
    Row(
        modifier
            .clip(shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onToggle
            )
    ) {
        Text(
            text = leftText,
            modifier = if (leftChecked) Modifier.background(color).padding(padding)
            else Modifier.background(Color.White).padding(padding),
            color = if (leftChecked) contentColor else Color.Gray,
            style = style
        )
        Text(
            text = rightText,
            modifier = if (!leftChecked) Modifier.background(color).padding(padding)
            else Modifier.background(Color.White).padding(padding),
            color = if (!leftChecked) contentColor else Color.Gray,
            style = style
        )
    }
}