package com.kasuminotes.data

import androidx.compose.runtime.Stable
import com.kasuminotes.R

@Stable
class Property(init: (Int) -> Double) {

    constructor(pairs: List<Pair<Int, Double>>) : this({ 0.0 }) {
        pairs.forEach { pair ->
            arr[pair.first - 1] = pair.second
        }
    }

    private val arr = DoubleArray(size, init)
//    val hp: Double get() = arr[0]
    val atk: Double get() = arr[1]
//    val def: Double get() = arr[2]
    val magicStr: Double get() = arr[3]
//    val magicDef: Double get() = arr[4]
//    val physicalCritical: Double get() = arr[5]
//    val magicCritical: Double get() = arr[6]
//    val dodge: Double get() = arr[7]
//    val lifeSteal: Double get() = arr[8]
//    val waveHpRecovery: Double get() = arr[9]
//    val waveEnergyRecovery: Double get() = arr[10]
//    val physicalPenetrate: Double get() = arr[11]
//    val magicPenetrate: Double get() = arr[12]
//    val energyRecoveryRate: Double get() = arr[13]
//    val hpRecoveryRate: Double get() = arr[14]
//    val energyReduceRate: Double get() = arr[15]
//    val accuracy: Double get() = arr[16]

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
        val zero = Property { 0.0 }

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
