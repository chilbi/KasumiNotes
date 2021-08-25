package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class Position : ResId {
    All {
        @StringRes
        override val resId = R.string.all_position
    },
    Front {
        @StringRes
        override val resId = R.string.front
    },
    Center {
        @StringRes
        override val resId = R.string.center
    },
    Back {
        @StringRes
        override val resId = R.string.back
    }
}
