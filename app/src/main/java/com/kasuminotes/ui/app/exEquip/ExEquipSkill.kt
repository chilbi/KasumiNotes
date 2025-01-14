package com.kasuminotes.ui.app.exEquip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.action.ActionBuilder
import com.kasuminotes.action.D
import com.kasuminotes.action.annotatedStringDescription
import com.kasuminotes.action.getUnknown
import com.kasuminotes.action.stringDescription
import com.kasuminotes.data.Property
import com.kasuminotes.data.SkillData
import com.kasuminotes.ui.components.ActionLabel
import com.kasuminotes.ui.components.PlaceImage
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.VisibleIconButton
import com.kasuminotes.utils.UrlUtil

@Composable
fun ExEquipSkill(
    passiveSkill1: SkillData?,
    passiveSkill2: SkillData?,
    battleProperty: Property
) {
    LabelContainer(
        label = stringResource(R.string.equip_skill),
        color = MaterialTheme.colorScheme.primary
    ) {
        if (passiveSkill1 != null) {
            ExEquipSkillItem(passiveSkill1, battleProperty)
        }
        if (passiveSkill2 != null) {
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            ExEquipSkillItem(passiveSkill2, battleProperty)
        }
    }
}

@Composable
private fun ExEquipSkillItem(
    passiveSkill: SkillData,
    battleProperty: Property
) {
    var visible by remember { mutableStateOf(false) }
    val descriptionList: List<D> by remember(passiveSkill, battleProperty) {
        derivedStateOf {
            ActionBuilder(passiveSkill.rawDepends, passiveSkill.actions, true)
                .buildDescriptionList(0, battleProperty)
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
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        VisibleIconButton(
            visible = visible,
            imageVector = Icons.Filled.Code,
            onClick = { visible = !visible }
        )
    }

    if (visible) {
        Text(
            text = passiveSkill.description,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        passiveSkill.actions.forEachIndexed { index, action ->
            Row(Modifier.padding(4.dp)) {
                ActionLabel(index + 1)
                Text(
                    text = stringDescription(action.getUnknown()),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        descriptionList.forEachIndexed { index, d ->
            Row(Modifier.padding(4.dp)) {
                ActionLabel(index + 1)
                Text(
                    text = annotatedStringDescription(stringDescription(d)),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
