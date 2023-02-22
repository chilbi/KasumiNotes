package com.kasuminotes.data

data class ExSkillData(
    val exActions: List<SkillAction>,
    val exEvolutionActions: List<SkillAction>
) {
    fun getProperty(rarity: Int, exLevel: Int): Property {
        val actions = if (rarity > 4) exEvolutionActions else exActions

        return if (actions.isEmpty()) {
            Property.zero
        } else {
            val pairs = mutableListOf<Pair<Int, Double>>()
            actions.forEach { action ->
                val value = action.actionValue2 + action.actionValue3 * exLevel
                pairs.add(action.actionDetail1 to value)
            }
            Property(pairs)
        }
    }
}
