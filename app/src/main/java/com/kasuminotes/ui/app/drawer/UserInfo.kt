package com.kasuminotes.ui.app.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.MaxUserData
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells

@Composable
fun UserInfo(maxUserData: MaxUserData?) {
    if (maxUserData == null) return

    val pairs: List<Pair<String, String>> = listOf(
        stringResource(R.string.level) to maxUserData.maxCharaLevel.toString(),
        stringResource(R.string.rank) to maxUserData.maxPromotionLevel.toString(),
        stringResource(R.string.map) to maxUserData.maxArea.toString(),
        stringResource(R.string.chara) to "${maxUserData.userChara}/${maxUserData.maxChara}",
        stringResource(R.string.unique) to "${maxUserData.userUnique}/${maxUserData.maxUnique}",
        stringResource(R.string.rarity_6) to "${maxUserData.userRarity6}/${maxUserData.maxRarity6}"
    )

    VerticalGrid(
        size = pairs.size,
        cells = VerticalGridCells.Fixed(3)
    ) { index ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pair = pairs[index]

            Text(
                text = pair.first,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = pair.second,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
