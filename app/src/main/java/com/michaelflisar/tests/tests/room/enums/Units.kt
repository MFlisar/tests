package com.michaelflisar.tests.tests.room.enums

object Units {

    enum class Weight(
        val label: String,
        val factorLarge: Int,
        val labelLarge: String
    ) {
        KG("kg", 1000, "t"), // 1000kg == 1t
        LBS("lb", 2000, "ton") // 2000lb == 1ton
        ;

        fun other() = if (this == KG) LBS else KG
    }

    enum class Length {
        CM,
        FEET_AND_INCHES
    }

    enum class Calories {
        KCAL,
        KJ
    }
}