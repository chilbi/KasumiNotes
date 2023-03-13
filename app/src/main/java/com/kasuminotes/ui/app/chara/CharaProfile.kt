package com.kasuminotes.ui.app.chara

import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kasuminotes.R
import com.kasuminotes.data.UnitData
import com.kasuminotes.ui.components.Container
import com.kasuminotes.ui.components.Infobar
import com.kasuminotes.ui.components.MultiLineText

@Composable
fun CharaProfile(
    unitData: UnitData,
    state: ScrollState
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(4.dp)
    ) {
        Container {
            InfobarFullWidth(R.string.actual_name, unitData.actualName)

            InfobarFullWidth(R.string.kana, unitData.kana)

            InfobarFullWidth(R.string.voice, unitData.voice)

            InfobarFullWidth(R.string.guild, unitData.guild)

            InfobarFullWidth(R.string.favorite, unitData.favorite)

            InfobarFullWidth(R.string.race, unitData.race)

            Row {
                InfobarHalfWidth(R.string.height, unitData.height)

                InfobarHalfWidth(R.string.weight, unitData.weight)
            }

            Row {
                InfobarHalfWidth(R.string.age, unitData.age)

                InfobarHalfWidth(
                    R.string.birthday,
                    stringResource(R.string.birth_month_day, unitData.birthMonth, unitData.birthDay)
                )
            }

            Row {
                InfobarHalfWidth(R.string.blood_type, unitData.bloodType)

                InfobarHalfWidth(R.string.chara_id, (unitData.unitId / 100).toString())
            }
        }

        MultiLineInfo(R.string.comment, unitData.comment)

        if (unitData.catchCopy.isNotEmpty()) {
            MultiLineInfo(R.string.catch_copy, unitData.catchCopy)
        }

        if (unitData.selfText.isNotEmpty()) {
            MultiLineInfo(R.string.self_text, unitData.selfText)
        }
    }
}

@Composable
private fun MultiLineInfo(
    @StringRes labelResId: Int,
    text: String
) {
    Container {
        Text(
            text = stringResource(labelResId),
            modifier = Modifier
                .padding(4.dp)
                .width(dimensionResource(R.dimen.multi_label_width))
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(vertical = 2.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )

        MultiLineText(text)
    }
}

@Composable
private fun RowScope.InfobarHalfWidth(
    @StringRes labelResId: Int,
    value: String
) {
    Infobar(
        label = stringResource(labelResId),
        value = value,
        modifier = Modifier.weight(1f, true)
    )
}

@Composable
private fun InfobarFullWidth(
    @StringRes labelResId: Int,
    value: String
) {
    Infobar(
        label = stringResource(labelResId),
        value = value
    )
}
