package com.kasuminotes.ui.app.quest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.R
import com.kasuminotes.data.QuestData
import com.kasuminotes.data.RewardData
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.VerticalGrid
import com.kasuminotes.ui.components.VerticalGridCells
import com.kasuminotes.ui.components.selectedContainerColor
import com.kasuminotes.utils.Helper
import com.kasuminotes.utils.UrlUtil

@Composable
fun QuestDropList(
    questDataList: List<QuestData>,
    selectedList: List<Int>? = null,
    onSelected: ((rewardId: Int) -> Unit)? = null
) {
    questDataList.forEach { questData ->
        QuestDropItem(
            questData = questData,
            selectedList = selectedList,
            onSelected = onSelected
        )
    }
}

@Composable
fun QuestDropItem(
    questData: QuestData,
    selectedList: List<Int>? = null,
    onSelected: ((rewardId: Int) -> Unit)? = null
) {
    val dropList = questData.getDropList()
    val dropGold = questData.getDropGold()

    Container {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuestLabel(Helper.getQuestType(questData.questId))

            Spacer(Modifier.width(4.dp))

            Text(
                text = questData.questName,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.mana),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )

            Text(
                text = dropGold.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        VerticalGrid(
            size = dropList.size,
            cells = VerticalGridCells.Adaptive(44.dp)
        ) { index ->
            val dropItem = dropList[index]
            RewardItem(
                rewardData = dropItem,
                selected = selectedList?.contains(dropItem.rewardId) ?: false,
                onSelected = onSelected
            )
        }
    }
}

@Composable
private fun RewardItem(
    rewardData: RewardData,
    selected: Boolean = false,
    onSelected: ((rewardId: Int) -> Unit)? = null
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .selectedContainerColor(selected)
                .clickable(enabled = onSelected != null) {
                    onSelected?.invoke(rewardData.rewardId)
                }
        ) {
            Box(Modifier.size(40.dp)) {
                PlaceImage(
                    if (rewardData.rewardType == 4) UrlUtil.getEquipIconUrl(rewardData.rewardId)
                    else UrlUtil.getItemIconUrl(rewardData.rewardId)
                )
            }
            Text(
                text = "${rewardData.odds}%",
                fontSize = 12.sp,
                modifier = Modifier
                    .width(40.dp)
                    .padding(vertical = 2.dp),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
