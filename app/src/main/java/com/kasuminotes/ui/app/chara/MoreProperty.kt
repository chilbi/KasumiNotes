package com.kasuminotes.ui.app.chara

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.state.CharaState
import com.kasuminotes.ui.components.LabelContainer
import com.kasuminotes.ui.components.PropertyTable

@Composable
fun MoreProperty(charaState: CharaState) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        CheckboxContainer(
            checked = true,
            onCheckedChange = {},
            enabled = false
        ) {
            LabelContainer(
                label = stringResource(R.string.base_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                PropertyTable(charaState.baseProperty)
            }
        }

        CheckboxContainer(
            checked = true,
            onCheckedChange = {},
            enabled = false
        ) {
            LabelContainer(
                label = stringResource(R.string.rank_bonus_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                if (charaState.rankBonusProperty == null || charaState.rankBonusProperty == Property.zero) {
                    Text(stringResource(R.string.no_data))
                } else {
                    PropertyTable(
                        property = charaState.rankBonusProperty!!,
                        indices = charaState.rankBonusProperty!!.nonzeroIndices
                    )
                }
            }
        }

        CheckboxContainer(
            checked = charaState.includeExSkill,
            onCheckedChange = charaState::changeIncludeExSkill
        ) {
            LabelContainer(
                label = stringResource(R.string.ex_skill_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                if (charaState.exSkillProperty == Property.zero) {
                    Text(stringResource(R.string.no_data))
                } else {
                    PropertyTable(
                        property = charaState.exSkillProperty,
                        indices = charaState.exSkillProperty.nonzeroIndices
                    )
                }
            }
        }

        CheckboxContainer(
            checked = charaState.includeExEquip,
            onCheckedChange = charaState::changeIncludeExEquip
        ) {
            LabelContainer(
                label = stringResource(R.string.ex_equip_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                if (charaState.exEquipProperty == Property.zero) {
                    Text(stringResource(R.string.no_data))
                } else {
                    PropertyTable(
                        property = charaState.exEquipProperty,
                        indices = charaState.exEquipProperty.nonzeroIndices
                    )
                }
            }
        }

        CheckboxContainer(
            checked = charaState.includeExEquipSkill,
            onCheckedChange = charaState::changeIncludeExEquipSkill
        ) {
            LabelContainer(
                label = stringResource(R.string.ex_equip_skill_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                if (charaState.exEquipSkillProperty == Property.zero) {
                    Text(stringResource(R.string.no_data))
                } else {
                    PropertyTable(
                        property = charaState.exEquipSkillProperty,
                        indices = charaState.exEquipSkillProperty.nonzeroIndices
                    )
                }
            }
        }

        CheckboxContainer(
            checked = charaState.includeConnectRank,
            onCheckedChange = charaState::changeIncludeConnectRank
        ) {
            LabelContainer(
                label = stringResource(R.string.connect_rank_property),
                color = MaterialTheme.colorScheme.primary,
                padding = 12.dp
            ) {
                if (charaState.connectRankProperty == Property.zero) {
                    Text(stringResource(R.string.no_data))
                } else {
                    PropertyTable(
                        property = charaState.connectRankProperty,
                        indices = charaState.connectRankProperty.nonzeroIndices
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxContainer(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        content()
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
            enabled = enabled
        )
    }
}
