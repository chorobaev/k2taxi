package io.jachoteam.taxiappclient.data.repository.implementation

import androidx.lifecycle.LiveData
import io.jachoteam.taxiappclient.data.local.AuthPreference
import io.jachoteam.taxiappclient.data.local.OrderPreference
import io.jachoteam.taxiappclient.data.local.TariffPreference
import io.jachoteam.taxiappclient.data.network.OrderClient
import io.jachoteam.taxiappclient.data.repository.OrderRepository
import io.jachoteam.taxiappclient.models.local.Driver
import io.jachoteam.taxiappclient.models.local.Order
import io.jachoteam.taxiappclient.models.local.Tariff
import io.reactivex.Single

class OrderRepositoryImpl(
    private val userClient: OrderClient,
    private val tariffPreference: TariffPreference,
    private val orderPreference: OrderPreference,
    private val authPreference: AuthPreference
) : OrderRepository {

    private val token get() = authPreference.authToken ?: ""

    override val timeUntilNextOrder: LiveData<Long>
        get() = orderPreference.timeUntilNextOrder

    override val tariffs: LiveData<List<Tariff>>
        get() = tariffPreference.tariffsLiveData

    override fun requestRide(request: Order): Single<String> {
        return userClient.requestRide(
            token,
            request.tariffId,
            request.address,
            request.phoneUID,
            request.phone,
            request.latitude,
            request.longitude
        ).doOnSuccess {
            orderPreference.addRequest()
        }
    }

    override fun getDrivers(): Single<List<Driver>> {
        return userClient.getDrivers(token)
    }

    override fun requestTariffs(): Single<List<Tariff>> {
        return userClient.getTariffs(token)
            .doOnSuccess {
                tariffPreference.tariffs = it
            }
    }
}