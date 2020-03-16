package io.jachoteam.taxiappclient.models.local

import android.location.Location

data class K2Location(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val provider: String
)

fun Location.toK2Location(): K2Location =
    K2Location(latitude, longitude, altitude, provider)

fun K2Location.toLocation(): Location =
    Location(provider).also { location ->
        location.latitude = this.latitude
        location.longitude = this.longitude
        location.altitude = this.altitude
    }
