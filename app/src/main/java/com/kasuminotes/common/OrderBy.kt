package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class OrderBy : ResId {
    StartTime {
        @StringRes
        override val resId = R.string.start_time
    },
    ID {
        @StringRes
        override val resId = R.string.chara_id
    },
    Rarity {
        @StringRes
        override val resId = R.string.rarity
    },
    SearchAreaWidth {
        @StringRes
        override val resId = R.string.search_area_width
    },
    Age {
        @StringRes
        override val resId = R.string.age
    },
    Height {
        @StringRes
        override val resId = R.string.height
    },
    Weight {
        @StringRes
        override val resId = R.string.weight
    }
}
