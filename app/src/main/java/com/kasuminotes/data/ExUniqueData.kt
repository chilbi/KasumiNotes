package com.kasuminotes.data

data class ExUniqueData(
    val equipmentId: Int,
    val requiredItemId: Int,
    val requiredItemCount: Int,
    val property: Property
) {
    fun getProperty(userData: UserData, connectRankData: ConnectRankData?): Property {
        return if (
            userData.unique1Level >= 370 &&
            connectRankData != null &&
            connectRankData.getExUnique1Equipable(userData.connectRank)) {
            property
        } else {
            Property.zero
        }
    }

    companion object {
        private var fields: String? = null

        fun getFields(): String {
            if (fields == null) {
                fields = "hp," +
                        "attack," +
                        "defense," +
                        "magic_attack," +
                        "magic_defense," +
                        "critical," +
                        "magic_critical," +
                        "dodge," +
                        "life_steal," +
                        "wave_hp_recovery," +
                        "wave_energy_recovery," +
                        "penetration," +
                        "magic_penetration," +
                        "energy_recovery_rate," +
                        "hp_recovery_rate," +
                        "energy_reduce_rate," +
                        "accuracy," +
                        "equipment_id," +
                        "required_item_id," +
                        "required_item_count"
            }
            return fields!!
        }
    }
}
