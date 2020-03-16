package io.jachoteam.taxiappclient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jachoteam.taxiappclient.base.BaseViewModel
import io.jachoteam.taxiappclient.data.repository.AuthRepository
import io.jachoteam.taxiappclient.data.repository.LocationsRepository
import io.jachoteam.taxiappclient.di.util.SMSVerification
import io.jachoteam.taxiappclient.models.TimeCounter
import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.toLocation
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.models.network.VerificationResponse
import io.jachoteam.taxiappclient.utilities.SingleLiveEvent
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationsRepository: LocationsRepository,
    @SMSVerification private val timeCounter: TimeCounter
) : BaseViewModel() {

    private val _shouldGoVerifyFragment = SingleLiveEvent<Unit>()
    private val _shouldGoRegisterFragment = SingleLiveEvent<Unit>()
    private val _authenticationSuccess = SingleLiveEvent<Unit>()
    private val _cities = MutableLiveData<List<CityInfo>>()
    private val _phoneNumber = MutableLiveData<String>()

    val shouldGoVerifyFragment: LiveData<Unit> get() = _shouldGoVerifyFragment
    val shouldGoRegisterFragment: LiveData<Unit> get() = _shouldGoRegisterFragment
    val authenticationSuccess: LiveData<Unit> get() = _authenticationSuccess
    val cities: LiveData<List<CityInfo>> get() = _cities
    val phoneNumber: LiveData<String> get() = _phoneNumber
    val isPhoneNumberVerified: Boolean get() = authRepository.isUserVerified()
    val timeToResendCode: LiveData<TimeCounter.Time> get() = timeCounter

    fun signIn(phoneNumber: String, shouldStartVerifyFragment: Boolean = true) {
        _phoneNumber.value = phoneNumber
        authRepository.signIn(phoneNumber).request {
            if (shouldStartVerifyFragment) {
                _shouldGoVerifyFragment.call()
            }
            timeCounter.start()
        }
    }

    fun verifySMSCode(code: String) {
        authRepository.verify(code).request {
            processVerificationResponse(it)
        }
    }

    private fun processVerificationResponse(response: VerificationResponse) {
        if (response.isRegistered) {
            _authenticationSuccess.call()
        } else {
            _shouldGoRegisterFragment.call()
        }
    }

    fun requestCities() {
        if (cities.value.isNullOrEmpty()) {
            authRepository.cities.request {
                onCitiesReceived(it)
            }
        }
    }

    private fun onCitiesReceived(cities: List<CityInfo>) {
        val currentLocation = locationsRepository.location.value
        var sortedCities = cities
        currentLocation?.let { locationOfDevice ->
            sortedCities = cities.sortedBy {
                locationOfDevice.distanceTo(it.center.toLocation())
            }
        }
        _cities.value = sortedCities
    }

    fun updateUserInformation(name: String, city: City) {
        authRepository.updateUserInfo(name, city).request {
            _authenticationSuccess.call()
        }
    }
}