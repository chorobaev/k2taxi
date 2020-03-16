package io.jachoteam.taxiappclient.models.network

import io.jachoteam.taxiappclient.models.local.User

data class VerificationResponse(
    val k2token: String,
    val user: User
) {
    val isRegistered: Boolean
        get() = user.city != null
}