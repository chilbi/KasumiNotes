package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.ui.components.FixedWidthLabel
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.ui.components.Rarities

@Composable
fun EquipProperty(
    enhanceLevel: Int,
    maxEnhanceLevel: Int,
    baseProperty: Property,
    property: Property,
    onEnhanceLevelChange: (Int) -> Unit
) {
    Container {
        PropertyTable(
            property = property,
            indices = property.nonzeroIndices,
            originProperty = if (maxEnhanceLevel > 0) baseProperty else null
        )

        if (maxEnhanceLevel > 5) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FixedWidthLabel(stringResource(R.string.promotion_level))

                var value by rememberSaveable(enhanceLevel) {
                    mutableStateOf(enhanceLevel.toFloat())
                }

                val intValue = value.toInt()

                Text(
                    text = intValue.toString(),
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )

                IconButton(
                    onClick = { onEnhanceLevelChange(enhanceLevel - 1) },
                    enabled = enhanceLevel > 1
                ) {
                    Icon(Icons.Filled.Remove, null)
                }

                Slider(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.weight(1f),
                    valueRange = 1f..maxEnhanceLevel.toFloat(),
                    onValueChangeFinished = { onEnhanceLevelChange(intValue) }
                )

                IconButton(
                    onClick = { onEnhanceLevelChange(enhanceLevel + 1) },
                    enabled = enhanceLevel < maxEnhanceLevel
                ) {
                    Icon(Icons.Filled.Add, null)
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FixedWidthLabel(stringResource(R.string.promotion_level))
                Spacer(Modifier.width(8.dp))
                Rarities(
                    highlightCount = 0,
                    maxRarity = maxEnhanceLevel,
                    rarity = enhanceLevel,
                    onRarityChange = onEnhanceLevelChange
                )
            }
        }
    }
}