package io.jachoteam.taxiappclient.models.local

import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.models.local.TariffIcon.*

data class Tariff(
        var id: Int = 0,
        val name: String = "",
        val base: Double = 0.0,
        val cost: Double = 0.0,
        val icon: TariffIcon = STANDART
)

enum class TariffIcon {
    STANDART,
    COMFORT,
    MINIVEN,
    PORTER
}

val Tariff.tariffIcon: Int
    get() = when (icon) {
        STANDART -> R.drawable.standard
        COMFORT -> R.drawable.comfort
        MINIVEN -> R.drawable.minivan
        PORTER -> R.drawable.porter
    }
