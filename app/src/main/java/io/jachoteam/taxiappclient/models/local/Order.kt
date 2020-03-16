package io.jachoteam.taxiappclient.models.local

data class Order(
    val tariffId: Int = -1,
    val phone: String = "",
    val phoneUID: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)