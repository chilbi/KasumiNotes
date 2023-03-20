package com.kasuminotes.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R

@Composable
fun MultiLineText(
    multiLine: String,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    multiLine.split("\\n").forEach { line ->
        LineText(line, padding, textAlign, style)
    }
}

@Composable
fun MultiLineText(
    lineList: List<String>,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    lineList.forEach { line ->
        LineText(line, padding, textAlign, style)
    }
}

@Composable
fun LineText(
    line: String,
    padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = line,
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        textAlign = textAlign,
        style = style
    )
}

@Composable
fun NoDataText() {
    Text(
        text = stringResource(R.string.no_data),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )
}
