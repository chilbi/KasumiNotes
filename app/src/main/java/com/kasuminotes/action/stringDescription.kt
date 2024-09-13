package com.kasuminotes.action

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import java.lang.StringBuilder

sealed class D {
    object Unknown : D()

    class Text(val value: String) : D()

    class Format(
        @StringRes val id: Int,
        val args: Array<D>? = null
    ) : D()

    class Join(val args: Array<D>) : D()

    fun copy(): D {
        return when (this) {
            Unknown -> Unknown
            is Text -> Text(this.value)
            is Format -> Format(this.id, this.args)
            is Join -> Join(this.args)
        }
    }

    fun append(d: D): D {
        return Join(arrayOf(this.copy(), d))
    }

    fun insert(d: D): D {
        return Join(arrayOf(d, this.copy()))
    }

    companion object {
        fun join(@StringRes vararg args: Int): D {
            return Join(args.map { Format(it) }.toTypedArray())
        }
    }
}

@Composable
@ReadOnlyComposable
fun stringDescription(d: D): String {
    return when (d) {
        D.Unknown -> "???"
        is D.Text -> d.value
        is D.Format -> {
            if (d.args == null) {
                stringResource(d.id)
            } else {
                val formatArgs = d.args.map { stringDescription(it) }.toTypedArray()
                stringResource(d.id, *formatArgs)
            }
        }
        is D.Join -> {
            val str = StringBuilder()
            d.args.forEach {
                str.append(stringDescription(it))
            }
            str.toString()
        }
    }
}

fun annotatedStringDescription(xmlString: String): AnnotatedString {
    return buildAnnotatedString {
        append()
    }
}
