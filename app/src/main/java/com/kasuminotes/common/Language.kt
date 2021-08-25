package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class Language : ResId {
    CN {
        @StringRes
        override val resId = R.string.lang_cn
    },
    JP {
        @StringRes
        override val resId = R.string.lang_jp
    }
}
