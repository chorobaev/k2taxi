package io.jachoteam.taxiappclient.data.network

import io.jachoteam.taxiappclient.models.local.Driver
import io.jachoteam.taxiappclient.models.local.Tariff
import io.jachoteam.taxiappclient.utilities.TOKEN
import io.jachoteam.taxiappclient.utilities.VERSION
import io.reactivex.Single
import retrofit2.http.*

interface OrderClient {

    @GET("${VERSION}/mobile/tariffs")
    fun getTariffs(
        @Header(TOKEN) token: String
    ): Single<List<Tariff>>

    @GET("${VERSION}/mobile/drivers")
    fun getDrivers(
        @Header(TOKEN) token: String
    ): Single<List<Driver>>

    @FormUrlEncoded
    @POST("${VERSION}/mobile/orders/create")
    fun requestRide(
        @Header(TOKEN) token: String,
        @Field("tariffId") tariffId: Int,
        @Field("address") address: String,
        @Field("phoneUID") phoneUID: String,
        @Field("phone") phone: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Single<String>
}