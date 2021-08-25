package com.kasuminotes.data

import androidx.compose.runtime.Stable
import com.kasuminotes.R

@Stable
class Property(init: (Int) -> Double = { 0.0 }) {

    constructor(pairs: List<Pair<Int, Double>>) : this() {
        pairs.forEach { pair ->
            arr[pair.first - 1] = pair.second
        }
    }

    private val arr = DoubleArray(size, init)

    operator fun get(index: Int) = arr[index]

    operator fun iterator(): DoubleIterator = arr.iterator()

    val nonzeroIndices: List<Int> by lazy {
        val list = mutableListOf<Int>()
        arr.indices.forEach { index ->
            if (arr[index] != 0.0) {
                list.add(index)
            }
        }
        list
    }

    override fun equals(other: Any?): Boolean {
        return other is Property && arr.indices.all { index ->
            this[index] == other[index]
        }
    }

    override fun hashCode(): Int {
        return arr.indices.sumOf { index -> this[index] }.toInt()
    }

    companion object {
         const val size = 17

        fun getStrRes(index: Int): Int = when (index) {
            0 -> R.string.hp
            1 -> R.string.atk
            2 -> R.string.def
            3 -> R.string.magic_str
            4 -> R.string.magic_def
            5 -> R.string.physical_critical
            6 -> R.string.magic_critical
            7 -> R.string.dodge
            8 -> R.string.life_steal
            9 -> R.string.wave_hp_recovery
            10 -> R.string.wave_energy_recovery
            11 -> R.string.physical_penetrate
            12 -> R.string.magic_penetrate
            13 -> R.string.energy_recovery_rate
            14 -> R.string.hp_recovery_rate
            15 -> R.string.energy_reduce_rate
            16 -> R.string.accuracy
            else -> throw IndexOutOfBoundsException("The index range should be 0-${size - 1}")
        }

        val keys: Array<String>
            get() = arrayOf(
                "hp",
                "atk",
                "def",
                "magic_str",
                "magic_def",
                "physical_critical",
                "magic_critical",
                "dodge",
                "life_steal",
                "wave_hp_recovery",
                "wave_energy_recovery",
                "physical_penetrate",
                "magic_penetrate",
                "energy_recovery_rate",
                "hp_recovery_rate",
                "energy_reduce_rate",
                "accuracy"
            )

        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = keys.joinToString(",")
            }
            return fields!!
        }
    }
}

//val Property.hp: Double get() = this[0]
val Property.atk: Double get() = this[1]
//val Property.def: Double get() = this[2]
val Property.magicStr: Double get() = this[3]
//val Property.magicDef: Double get() = this[4]
//val Property.physicalCritical: Double get() = this[5]
//val Property.magicCritical: Double get() = this[6]
//val Property.dodge: Double get() = this[7]
//val Property.lifeSteal: Double get() = this[8]
//val Property.waveHpRecovery: Double get() = this[9]
//val Property.waveEnergyRecovery: Double get() = this[10]
//val Property.physicalPenetrate: Double get() = this[11]
//val Property.magicPenetrate: Double get() = this[12]
//val Property.energyRecoveryRate: Double get() = this[13]
//val Property.hpRecoveryRate: Double get() = this[14]
//val Property.energyReduceRate: Double get() = this[15]
//val Property.accuracy: Double get() = this[16]
