package io.jachoteam.taxiappclient.data.repository

import androidx.lifecycle.LiveData
import io.jachoteam.taxiappclient.models.local.Driver
import io.jachoteam.taxiappclient.models.local.Order
import io.jachoteam.taxiappclient.models.local.Tariff
import io.reactivex.Single

interface OrderRepository {

    val timeUntilNextOrder: LiveData<Long>
    val tariffs: LiveData<List<Tariff>>

    fun requestTariffs(): Single<List<Tariff>>

    fun requestRide(request: Order): Single<String>
    fun getDrivers(): Single<List<Driver>>
}