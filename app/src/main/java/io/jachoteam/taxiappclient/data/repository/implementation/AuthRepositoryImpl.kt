package io.jachoteam.taxiappclient.data.repository.implementation

import com.google.android.gms.maps.model.LatLng
import io.jachoteam.taxiappclient.data.local.*
import io.jachoteam.taxiappclient.data.network.AuthClient
import io.jachoteam.taxiappclient.data.repository.AuthRepository
import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.models.network.SimpleResponse
import io.jachoteam.taxiappclient.models.network.VerificationResponse
import io.reactivex.Single

class AuthRepositoryImpl(
    private val authClient: AuthClient,
    private val authPreference: AuthPreference,
    private val locationsPreference: LocationsPreference,
    private val orderPreference: OrderPreference,
    private val tariffPreference: TariffPreference,
    private val userPreference: UserPreference
) : AuthRepository {

    private var tempPhoneNumber: String? = null

    override val authToken: String? get() = authPreference.authToken ?: ""

    override val phoneNumber: String? get() = authPreference.phoneNumber

    override val cityLatLng: LatLng get() = authPreference.cityLatLng

    override val cities: Single<List<CityInfo>> get() = authClient.getCities()

    override val user: User? get() = userPreference.user

    override fun signIn(phoneNumber: String): Single<SimpleResponse> {
        tempPhoneNumber = null
        return authClient.signIn("0$phoneNumber")
            .doOnSuccess {
                tempPhoneNumber = phoneNumber
            }
    }

    override fun verify(code: String): Single<VerificationResponse> {
        return authClient.verifyPhoneNumber(tempPhoneNumber!!, code)
            .doOnSuccess {
                authPreference.phoneNumber = tempPhoneNumber
                authPreference.authToken = it.k2token
                tempPhoneNumber = null
                if (it != null && it.isRegistered) {
                    userPreference.user = it.user
                }
            }
    }

    override fun updateUserInfo(name: String, city: City): Single<SimpleResponse> {
        return authClient.updateUserInfo(authToken ?: "", city, name)
            .doOnSuccess {
                userPreference.user = User(name, city)
                authPreference.cityLatLng = city.latLng
            }
    }

    override fun saveCityLatLng(latLng: LatLng) {
        authPreference.cityLatLng = latLng
    }

    override fun isUserVerified(): Boolean {
        return authPreference.isLoggedIn
    }

    override fun isUserRegistered(): Boolean {
        return userPreference.isUserRegistered
    }

    override fun logout() {
        authPreference.logout()
        locationsPreference.clear()
        orderPreference.clear()
        tariffPreference.clear()
        userPreference.clear()
    }
}