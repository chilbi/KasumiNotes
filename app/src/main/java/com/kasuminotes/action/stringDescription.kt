package com.kasuminotes.action

import android.util.Xml
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

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

    fun style(
        primary: Boolean = false,
        bold: Boolean = false,
        underline: Boolean = false
    ): D {
        val tagStart = "<style primary=\"${primary}\" bold=\"${bold}\" underline=\"${underline}\">"
        val tagEnd = "</style>"
        return if (this is Text) Text(tagStart + this.value + tagEnd)
        else Join(arrayOf(Text(tagStart), this.copy(), Text(tagEnd)))
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

private data class DescStyle(
    val primary: Boolean,
    val bold: Boolean,
    val underline: Boolean
)

@Composable
@ReadOnlyComposable
fun annotatedStringDescription(xmlString: String): AnnotatedString {
    val parser = Xml.newPullParser()
    parser.setInput(StringReader("<p>$xmlString</p>"))
    return buildAnnotatedString {
        val styleMap = mutableMapOf<Int, DescStyle>()
        var styleNest = 0
        styleMap[0] = DescStyle(primary = false, bold = false, underline = false)
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "style") {
                        val prevDescStyle = styleMap[styleNest]!!
                        val primary = prevDescStyle.primary || parser.getAttributeValue(null, "primary") == "true"
                        val bold = prevDescStyle.bold || parser.getAttributeValue(null, "bold") == "true"
                        val underline = prevDescStyle.underline || parser.getAttributeValue(null, "underline") == "true"
                        styleNest++
                        styleMap[styleNest] = DescStyle(primary, bold, underline)
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "style") {
                        styleNest--
                    }
                }
                XmlPullParser.TEXT -> {
                    val descStyle = styleMap[styleNest]!!
                    withStyle(SpanStyle(
                        color = if (descStyle.primary) MaterialTheme.colorScheme.primary else Color.Unspecified,
                        fontWeight = if (descStyle.bold) FontWeight.Bold else null,
                        textDecoration = if (descStyle.underline) TextDecoration.Underline else null
                    )) {
                        append(parser.text)
                    }
                }
            }
            eventType = parser.next()
        }
    }
}
