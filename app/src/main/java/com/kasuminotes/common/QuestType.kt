package com.kasuminotes.common

import androidx.compose.ui.graphics.Color

private interface IQuestType {
    val value: Int
    val color: Color
}

enum class QuestType : IQuestType {
    N {
        override val value = 11000
        override val color = Color(0xFF69B4F3)
    },
    H {
        override val value = 12000
        override val color = Color(0xFFE35875)
    },
    VH {
        override val value = 13000
        override val color = Color(0xFFA456B9)
    },
    S {
        override val value = 18000
        override val color = Color(0xFF80E35C)
    }
}
