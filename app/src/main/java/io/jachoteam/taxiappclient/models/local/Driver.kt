package io.jachoteam.taxiappclient.models.local

import com.google.android.gms.maps.model.LatLng

data class Driver(
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

val Driver.latLng: LatLng
    get() = LatLng(latitude, longitude)