package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.ExEquipSubStatus
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.UnderlineStyle
import com.kasuminotes.ui.components.underline

@Composable
fun ExEquipSubProperty(
    subStatusList: List<ExEquipSubStatus>,
    subPercentList: List<Pair<Int, Double>>,
    valueDisplay: (index: Int, value: Double) -> String,
    onSubPercentListChange: () -> Unit,
    onSubPercentChange: (index: Int, status: Int) -> Unit,
    onSubPercentValueChange: (index: Int, value: Double) -> Unit
) {
    val statusItems = subStatusList.map { it.status }
    LabelContainer(
        label = stringResource(R.string.sub_property),
        color = MaterialTheme.colorScheme.primary
    ) {
        subPercentList.forEachIndexed { index, subPercent ->
            val valueItems = subStatusList.find { it.status == subPercent.first }!!.values
            val statusIndex = subPercent.first - 1
            val label = stringResource(Property.getStrRes(statusIndex))
            val value = valueDisplay(statusIndex, subPercent.second)

            SubPercentItem(
                index,
                statusIndex,
                statusItems,
                valueItems,
                label,
                value,
                valueDisplay,
                onSubPercentChange,
                onSubPercentValueChange
            )
        }

        Button(onSubPercentListChange, Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.forge))
        }
    }
}

@Composable
private fun SubPercentItem(
    index: Int,
    statusIndex: Int,
    statusItems: List<Int>,
    valueItems: List<Int>,
    label: String,
    value: String,
    valueDisplay: (index: Int, value: Double) -> String,
    onSubPercentChange: (index: Int, status: Int) -> Unit,
    onSubPercentValueChange: (index: Int, value: Double) -> Unit
) {
    val width = 150.dp
    val color = MaterialTheme.colorScheme.primary
    val contentColor = contentColorFor(color)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .underline(1.dp, color, UnderlineStyle.Dashed, width - 8.dp)
    ) {
        SelectMenu(
            Modifier
                .width(width)
                .background(color, MaterialTheme.shapes.extraSmall)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            Arrangement.SpaceBetween,
            contentColor,
            statusItems,
            { status -> onSubPercentChange(index, status) },
            { status ->
                Text(
                    text = stringResource(Property.getStrRes(status - 1)),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        ) {
            Text(
                text = label,
                color = contentColor,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium
            )
        }

        SelectMenu(
            Modifier.fillMaxWidth().padding(vertical = 2.dp),
            Arrangement.End,
            LocalContentColor.current,
            valueItems,
            { value -> onSubPercentValueChange(index, value.toDouble()) },
            { value ->
                Text(
                    text = valueDisplay(statusIndex, value.toDouble()),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        ) {
            Text(
                text = value,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
private fun <T>SelectMenu(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    iconTint: Color,
    items: List<T>,
    onChange: (item: T) -> Unit,
    itemDisplay: @Composable (item: T) -> Unit,
    content: @Composable () -> Unit
) {
    Box(Modifier.wrapContentSize()) {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier.clickable { expanded = true },
            horizontalArrangement,
            Alignment.CenterVertically
        ) {
            content()
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = iconTint
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 420.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { itemDisplay(item) },
                    onClick = {
                        onChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
