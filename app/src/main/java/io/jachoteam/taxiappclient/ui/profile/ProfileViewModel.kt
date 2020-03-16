package io.jachoteam.taxiappclient.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jachoteam.taxiappclient.base.BaseViewModel
import io.jachoteam.taxiappclient.data.repository.AuthRepository
import io.jachoteam.taxiappclient.data.repository.UserRepository
import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.utilities.SingleLiveEvent
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _shouldShowProfileEditFragment = SingleLiveEvent<Unit>()
    private val _shouldCloseProfileEditFragment = SingleLiveEvent<Unit>()
    private val _cities = MutableLiveData<List<CityInfo>>()

    val shouldShowProfileEditFragment: LiveData<Unit> get() = _shouldShowProfileEditFragment
    val shouldCloseProfileEditFragment: LiveData<Unit> get() = _shouldCloseProfileEditFragment
    val userLiveData: LiveData<User> get() = userRepository.userLiveData
    val cities: LiveData<List<CityInfo>> get() = _cities

    init {
        requestCities()
    }

    fun openProfileEditFragment() {
        _shouldShowProfileEditFragment.call()
    }

    fun updateUserInformation(name: String, city: City) {
        authRepository.updateUserInfo(name, city).request {
            _shouldCloseProfileEditFragment.call()
        }
    }

    fun requestCities() {
        val tempCities = cities.value
        if (tempCities.isNullOrEmpty()) {
            authRepository.cities.request {
                onCitiesReceived(it)
            }
        } else {
            onCitiesReceived(tempCities)
        }
    }

    private fun onCitiesReceived(cities: List<CityInfo>) {
        val sortedCities = cities.toMutableList()
        userLiveData.value?.city?.let { city ->
            cities.find { it.tag == city }?.let { currentCity ->
                sortedCities.remove(currentCity)
                sortedCities.add(0, currentCity)
            }
        }

        _cities.value = sortedCities
    }
}