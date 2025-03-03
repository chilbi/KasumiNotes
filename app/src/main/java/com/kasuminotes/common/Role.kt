package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class Role : ResId {
    All {
        @StringRes
        override val resId = R.string.all_role
    },
    Attacker {
        @StringRes
        override val resId = R.string.attacker
    },
    Breaker {
        @StringRes
        override val resId = R.string.breaker
    },
    Buffer {
        @StringRes
        override val resId = R.string.buffer
    },
    Debuffer {
        @StringRes
        override val resId = R.string.debuffer
    },
    Booster {
        @StringRes
        override val resId = R.string.booster
    },
    Healer {
        @StringRes
        override val resId = R.string.healer
    },
    Tank {
        @StringRes
        override val resId = R.string.tank
    },
    Jammer {
        @StringRes
        override val resId = R.string.jammer
    }
}
