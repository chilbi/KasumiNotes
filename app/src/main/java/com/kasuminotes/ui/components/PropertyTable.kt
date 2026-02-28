package com.kasuminotes.ui.components

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kasuminotes.R
import com.kasuminotes.data.Property
import java.math.BigDecimal
import java.util.Locale
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
    val context = LocalContext.current
    VerticalGrid(
        size = indices.size,
        cells = VerticalGridCells.Fixed(2),
        modifier = modifier
    ) { i ->
        val index = indices[i]
        val label = stringResource(Property.getStrRes(index))
        val value = property[index].roundToInt()// TODO 不确定的取整方式
        val valueDisplay = if (index == 0) value.formatHP(context) else value.toString()//HP

        if (originProperty == null) {
            Infobar(
                label = label,
                value = valueDisplay
            )
        } else {
            val originValue = originProperty[index].roundToInt()// TODO 不确定的取整方式
            val diffValue = value - originValue

            if (diffValue == 0) {
                Infobar(
                    label = label,
                    value = valueDisplay
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

fun Int.formatHP(context: Context): String {
    return when {
        this >= 1_0000_0000 -> {
            formatWithDivisor(
                context,
                1_0000_0000,
                R.string.unit_hundred_million
            )
        }
        this >= 1_0000 -> {
            formatWithDivisor(
                context,
                1_0000,
                R.string.unit_ten_thousand
            )
        }
        else -> this.toString()
    }
}

private fun Int.formatWithDivisor(context: Context, divisor: Int, unitResId: Int): String {
    val unit = context.getString(unitResId)
    val value = BigDecimal(this).divide(BigDecimal(divisor))
    return value.stripTrailingZeros().toPlainString() + unit
}
