package com.kasuminotes.common

import androidx.annotation.StringRes
import com.kasuminotes.R

enum class OrderBy : ResId {
    StartTime {
        @StringRes
        override val resId = R.string.start_time
    },
    CharaId {
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
//    Hp {
//        @StringRes
//        override val resId = R.string.hp
//    },
//    Atk {
//        @StringRes
//        override val resId = R.string.atk
//    },
//    MagicStr {
//        @StringRes
//        override val resId = R.string.magic_str
//    },
//    Def {
//        @StringRes
//        override val resId = R.string.def
//    },
//    MagicDef {
//        @StringRes
//        override val resId = R.string.magic_def
//    },
//    PhysicalCritical {
//        @StringRes
//        override val resId = R.string.physical_critical
//    },
//    MagicCritical {
//        @StringRes
//        override val resId = R.string.magic_critical
//    },
//    EnergyRecoveryRate {
//        @StringRes
//        override val resId = R.string.energy_recovery_rate
//    },
//    EnergyReduceRate {
//        @StringRes
//        override val resId = R.string.energy_reduce_rate
//    }
}
