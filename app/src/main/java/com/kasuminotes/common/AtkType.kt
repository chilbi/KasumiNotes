package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class AtkType : ResId {
    All {
        @StringRes
        override val resId = R.string.all_type
    },
    Physical {
        @StringRes
        override val resId = R.string.physical
    },
    Magic {
        @StringRes
        override val resId = R.string.magic
    }
}
