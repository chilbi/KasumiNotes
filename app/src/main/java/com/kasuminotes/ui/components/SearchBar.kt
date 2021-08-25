package com.kasuminotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
) {
    BasicTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
            .background(
                MaterialTheme.colors.onSurface.copy(TextFieldDefaults.BackgroundOpacity),
                CircleShape
            ),
        textStyle = TextStyle.Default.copy(color = LocalContentColor.current.copy(LocalContentAlpha.current)),
        singleLine = true,
        cursorBrush = SolidColor(LocalContentColor.current.copy(LocalContentAlpha.current)),
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp),
                    tint = LocalContentColor.current.copy(TextFieldDefaults.IconOpacity)
                )
                innerTextField()
            }
        }
    )
}