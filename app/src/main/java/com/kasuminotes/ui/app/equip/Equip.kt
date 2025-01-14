package com.kasuminotes.ui.app.equip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.state.DbState
import com.kasuminotes.state.EquipState

@Composable
fun Equip(
    dbState: DbState,
    equipState: EquipState,
    onBack: () -> Unit
) {
    var equipmentId = 0
    var equipmentName = ""
    var equipmentType = ""
    var description = ""
    var baseProperty = Property.zero

    when {
        equipState.equipData != null -> {
            val equip = equipState.equipData!!
            equipmentId = equip.equipmentId
            equipmentName = equip.equipmentName
            equipmentType = equip.equipmentType
            description = equip.description
            baseProperty = equip.baseProperty
        }
        equipState.unique1Data != null -> {
            val unique1 = equipState.unique1Data!!
            equipmentId = unique1.equipmentId
            equipmentName = unique1.equipmentName
            equipmentType = stringResource(R.string.unique_equip) + "1"
            description = unique1.description
            baseProperty = unique1.baseProperty
        }
        equipState.unique2Data != null -> {
            val unique2 = equipState.unique2Data!!
            equipmentId = unique2.equipmentId
            equipmentName = unique2.equipmentName
            equipmentType = stringResource(R.string.unique_equip) + "2"
            description = unique2.description
            baseProperty = unique2.baseProperty
        }
    }

    EquipScaffold(
        dbState,
        equipState,
        equipmentId,
        equipmentName,
        equipmentType,
        description,
        baseProperty,
        onBack
    )
}
