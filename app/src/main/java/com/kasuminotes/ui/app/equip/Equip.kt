package com.kasuminotes.ui.app.equip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kasuminotes.R
import com.kasuminotes.data.Property
import com.kasuminotes.ui.app.state.DbState
import com.kasuminotes.ui.app.state.EquipState

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
            val data = equipState.equipData!!
            equipmentId = data.equipmentId
            equipmentName = data.equipmentName
            equipmentType = data.equipmentType
            description = data.description
            baseProperty = data.baseProperty
        }
        equipState.uniqueData != null -> {
            val data = equipState.uniqueData!!
            equipmentId = data.equipmentId
            equipmentName = data.equipmentName
            equipmentType = stringResource(R.string.unique_equip)
            description = data.description
            baseProperty = data.baseProperty
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
