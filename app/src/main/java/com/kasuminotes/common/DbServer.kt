package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class DbServer : ResId {
    CN {
        @StringRes
        override val resId = R.string.server_cn
    },
    JP {
        @StringRes
        override val resId = R.string.server_jp
    }
}
