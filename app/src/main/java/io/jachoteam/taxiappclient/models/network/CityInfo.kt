package io.jachoteam.taxiappclient.models.network

import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.K2LatLng

data class CityInfo(
    val tag: City,
    val ru: String,
    val center: K2LatLng
)