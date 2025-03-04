package com.kasuminotes.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kasuminotes.data.Property
import kotlin.math.roundToInt

private val propertyIndices: List<Int> = listOf(
    1, 3, 5, 6, 2, 4, 11, 12, 16, 7, 0, 8, 9, 10, 13, 15, 14
)

@Composable
fun PropertyTable(
    property: Property,
    modifier: Modifier = Modifier,
    indices: List<Int> = propertyIndices,
    originProperty: Property? = null
) {
    VerticalGrid(
        size = indices.size,
        cells = VerticalGridCells.Fixed(2),
        modifier = modifier
    ) { i ->
        val index = indices[i]
        val label = stringResource(Property.getStrRes(index))
        val value = property[index].roundToInt()// TODO 不确定的取整方式

        if (originProperty == null) {
            Infobar(
                label = label,
                value = value.toString()
            )
        } else {
            val originValue = originProperty[index].roundToInt()// TODO 不确定的取整方式
            val diffValue = value - originValue

            if (diffValue == 0) {
                Infobar(
                    label = label,
                    value = value.toString()
                )
            } else {
                Infobar(
                    label = label,
                    value = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = if (diffValue > 0) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                }
                            )
                        ) {
                            if (diffValue > 0) append("+")
                            append("$diffValue ) ")
                        }
                        append(value.toString())
                    }
                )
            }
        }
    }
}
