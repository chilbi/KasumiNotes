package com.kasuminotes.ui.app.equip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.ExUniqueData
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PropertyTable
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExUnique(
    exUniqueData: ExUniqueData,
    exUniqueEquipable: Boolean,
    equipmentType: String,
    enhanceLevel: Int
) {
    LabelContainer(
        label = equipmentType + "SP",
        color = MaterialTheme.colorScheme.primary,
    ) {
        PropertyTable(
            property = exUniqueData.property,
            indices = exUniqueData.property.nonzeroIndices
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CraftItem(
                itemIdList = listOf(exUniqueData.requiredItemId),
                imageUrl = UrlUtil::getItemIconUrl,
                consumeSum = exUniqueData.requiredItemCount,
                selected = false,
                enabled = false,
                onClick = {}
            )
            Spacer(Modifier.weight(1f))
            val resId = if (exUniqueEquipable && enhanceLevel >= 370) {
                R.string.unlock
            } else {
                R.string.lock
            }
            Text(
                text = stringResource(resId),
                modifier = Modifier.padding(4.dp),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
