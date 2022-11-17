package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasuminotes.BuildConfig
import com.kasuminotes.R
import com.kasuminotes.action.ActionBuilder
import com.kasuminotes.action.D
import com.kasuminotes.action.getUnknown
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillData
import com.kasuminotes.ui.components.ActionLabel
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.UnderlineLabelColumn
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipSkill(
    passiveSkill1: SkillData?,
    passiveSkill2: SkillData?,
    baseProperty: Property
) {
    UnderlineLabelColumn(
        label = stringResource(R.string.equip_skill),
        color = MaterialTheme.colors.primary
    ) {
        if (passiveSkill1 != null) {
            ExEquipSkillItem(passiveSkill1, baseProperty)
        }
        if (passiveSkill2 != null) {
            Divider(Modifier.padding(vertical = 4.dp))
            ExEquipSkillItem(passiveSkill2, baseProperty)
        }
    }
}

@Composable
private fun ExEquipSkillItem(
    passiveSkill: SkillData,
    baseProperty: Property
) {
    val visibleTable = remember { mutableStateOf(false) }
    val descriptionList: List<D> by remember(passiveSkill, baseProperty) {
        derivedStateOf {
            ActionBuilder(
                passiveSkill.rawDepends,
                passiveSkill.actions,
                true
            ).buildDescriptionList(0, baseProperty)
        }
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(32.dp)) {
            PlaceImage(UrlUtil.getSkillIconUrl(passiveSkill.iconType))
        }
        Text(
            text = passiveSkill.name,
            modifier = Modifier.padding(start = 4.dp),
            fontSize = 16.sp
        )
        if (BuildConfig.DEBUG) {
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { visibleTable.value = !visibleTable.value }) {
                Icon(
                    if (visibleTable.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    null
                )
            }
        }
    }

    Text(
        text = passiveSkill.description,
        modifier = Modifier.padding(4.dp),
        fontSize = 14.sp
    )

    if (!visibleTable.value) {
        descriptionList.forEachIndexed { index, d ->
            Row(Modifier.padding(4.dp)) {
                ActionLabel(index + 1)

                Text(
                    text = stringDescription(d),
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 28.sp
                )
            }
        }
    }

    if (BuildConfig.DEBUG && visibleTable.value) {
        passiveSkill.actions.forEachIndexed { index, action ->
            Row(Modifier.padding(4.dp)) {
                ActionLabel(index + 1)

                Text(
                    text = stringDescription(action.getUnknown()),
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 28.sp
                )
            }
        }
    }
}
