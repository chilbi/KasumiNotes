package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class Element : ResId {
    All {
        @StringRes
        override val resId = R.string.all_element
    },
    Fire {
        @StringRes
        override val resId = R.string.fire
    },
    Water {
        @StringRes
        override val resId = R.string.water
    },
    Wind {
        @StringRes
        override val resId = R.string.wind
    },
    Light {
        @StringRes
        override val resId = R.string.light
    },
    Dark {
        @StringRes
        override val resId = R.string.dark
    }
}
