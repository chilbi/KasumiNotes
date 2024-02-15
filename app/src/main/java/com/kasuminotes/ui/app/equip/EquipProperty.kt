package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.kasuminotes.ui.components.SliderPlus

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
            SliderPlus(
                value = enhanceLevel,
                minValue = 1,
                maxValue = maxEnhanceLevel,
                onValueChange = onEnhanceLevelChange,
                startDecoration = {
                    FixedWidthLabel(stringResource(R.string.promotion_level))
                    Text(
                        text = it.toString(),
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
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
