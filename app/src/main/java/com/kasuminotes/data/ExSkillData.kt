package com.kasuminotes.data

data class ExSkillData(
    val exActions: List<SkillAction>,
    val exEvolutionActions: List<SkillAction>
) {
    fun getProperty(rarity: Int, exLevel: Int): Property {
        val pairs = mutableListOf<Pair<Int, Double>>()

        val actions = if (rarity > 4) exEvolutionActions else exActions

        actions.forEach { action ->
            val value = action.actionValue2 + action.actionValue3 * exLevel
            pairs.add(action.actionDetail1 to value)
        }

        return Property(pairs)
    }
}
