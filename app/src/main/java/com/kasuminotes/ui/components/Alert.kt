package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.ui.theme.DarkError
import com.kasuminotes.ui.theme.DarkInfo
import com.kasuminotes.ui.theme.DarkSuccess
import com.kasuminotes.ui.theme.DarkWarning
import com.kasuminotes.ui.theme.LightError
import com.kasuminotes.ui.theme.LightInfo
import com.kasuminotes.ui.theme.LightSuccess
import com.kasuminotes.ui.theme.LightWarning

enum class Severity {
    Error, Warning, Info, Success
}

@Composable
fun Alert(
    severity: Severity,
    text: String,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    actions: @Composable RowScope.() -> Unit = {}
) {
    val imageVector: ImageVector
    val color: Color
    val bgColor: Color
    when (severity) {
        Severity.Error -> {
            imageVector = Icons.Filled.ErrorOutline
            color = DarkError
            bgColor = LightError
        }
        Severity.Warning -> {
            imageVector = Icons.Filled.WarningAmber
            color = DarkWarning
            bgColor = LightWarning
        }
        Severity.Info -> {
            imageVector = Icons.Outlined.Info
            color = DarkInfo
            bgColor = LightInfo
        }
        Severity.Success -> {
            imageVector = Icons.Outlined.CheckCircle
            color = DarkSuccess
            bgColor = LightSuccess
        }
    }

    Row(
        modifier = modifier
            .background(bgColor, MaterialTheme.shapes.small)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = color,
        )

        BoxWithConstraints {
            Text(
                text = text,
                modifier = Modifier.widthIn(max = maxWidth - 48.dp),
                color = Color.Black,
                fontSize = 12.sp,
            )
        }

        Spacer(Modifier.weight(1f))

        actions()
    }
}
