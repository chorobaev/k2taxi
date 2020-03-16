package io.jachoteam.taxiappclient.data.repository

import com.google.android.gms.maps.model.LatLng
import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.models.network.SimpleResponse
import io.jachoteam.taxiappclient.models.network.VerificationResponse
import io.reactivex.Single

interface AuthRepository {

    val authToken: String?
    val phoneNumber: String?
    val cityLatLng: LatLng

    val cities: Single<List<CityInfo>>
    val user: User?

    fun signIn(phoneNumber: String): Single<SimpleResponse>
    fun verify(code: String): Single<VerificationResponse>
    fun updateUserInfo(name: String, city: City): Single<SimpleResponse>

    fun saveCityLatLng(latLng: LatLng)
    fun isUserVerified(): Boolean
    fun isUserRegistered(): Boolean
    fun logout()
}