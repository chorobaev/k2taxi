package io.jachoteam.taxiappclient.data.network

import io.jachoteam.taxiappclient.models.network.VersionResponse
import io.jachoteam.taxiappclient.utilities.OLD_VERSION
import io.jachoteam.taxiappclient.utilities.TOKEN
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface DeviceClient {

    @GET("$OLD_VERSION/version-info/client")
    fun getAppVersion(
        @Header(TOKEN) token: String
    ): Single<VersionResponse>
}