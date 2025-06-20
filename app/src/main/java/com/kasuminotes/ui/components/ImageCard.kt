package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageCard(
    imageUrl: String,
    primaryText: String,
    secondaryText: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    imageSize: Dp = 48.dp,
    primaryStyle: TextStyle = MaterialTheme.typography.titleMedium,
    secondaryStyle: TextStyle = MaterialTheme.typography.titleSmall,
    secondaryContent: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier.padding(paddingValues)) {
        Box(Modifier.size(imageSize)) {
            PlaceImage(imageUrl)
        }
        Column(
            modifier = Modifier
                .height(imageSize)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = primaryText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = primaryStyle,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = secondaryText,
                    color = LocalContentColor.current.copy(0.75f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = secondaryStyle
                )

                secondaryContent()
            }
        }
    }
}
