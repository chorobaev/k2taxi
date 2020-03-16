package io.jachoteam.taxiappclient.models.local

import android.location.Location

data class K2LatLng(
    val latitude: Double,
    val longitude: Double
)

fun K2LatLng.toLocation(): Location =
    Location("backend").also { location ->
        location.latitude = this.latitude
        location.longitude = this.longitude
    }