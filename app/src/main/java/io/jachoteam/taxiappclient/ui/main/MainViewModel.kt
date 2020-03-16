package io.jachoteam.taxiappclient.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import io.jachoteam.taxiappclient.base.BaseViewModel
import io.jachoteam.taxiappclient.data.repository.*
import io.jachoteam.taxiappclient.models.local.Driver
import io.jachoteam.taxiappclient.models.local.Order
import io.jachoteam.taxiappclient.models.local.Tariff
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.utilities.SingleLiveEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val locationsRepository: LocationsRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel(), LocationEmitter by locationsRepository {

    private val _divers = MutableLiveData<List<Driver>>()
    private val _rideRequestSucceeded = SingleLiveEvent<Unit>()
    private val _shouldMoveToLocation = SingleLiveEvent<LatLng>()
    private val _notification = SingleLiveEvent<String>()

    val tariffs: LiveData<List<Tariff>> get() = orderRepository.tariffs
    val drivers: LiveData<List<Driver>> get() = _divers
    val rideRequestSucceeded: LiveData<Unit> get() = _rideRequestSucceeded
    val timeUntilNextOrder: LiveData<Long> get() = orderRepository.timeUntilNextOrder
    val shouldMoveToLocation: LiveData<LatLng> get() = _shouldMoveToLocation
    val userLiveData: LiveData<User> get() = userRepository.userLiveData
    val notification: LiveData<String> get() = _notification

    val phoneNumber: String get() = authRepository.phoneNumber ?: ""
    val cityLatLng: LatLng get() = authRepository.cityLatLng

    fun getTariffId(index: Int): Int = try {
        tariffs.value?.get(index)?.id ?: -1
    } catch (e: IndexOutOfBoundsException) {
        -1
    }

    fun requestTariffs() {
        orderRepository.requestTariffs().request()
    }

    fun requestRide(order: Order) {
        orderRepository.requestRide(order).request {
            _rideRequestSucceeded.call()
        }
    }

    fun requestDrivers() {
        orderRepository.getDrivers().request {
            _divers.value = it
        }
    }

    fun moveToLocation(latLng: LatLng) {
        _shouldMoveToLocation.value = latLng
    }

    fun showNotification(msg: String) {
        _notification.value = msg
    }

    fun logout() {
        authRepository.logout()
    }
}