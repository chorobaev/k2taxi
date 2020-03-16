package io.jachoteam.taxiappclient.data.network

import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.models.network.SimpleResponse
import io.jachoteam.taxiappclient.models.network.VerificationResponse
import io.jachoteam.taxiappclient.utilities.TOKEN
import io.jachoteam.taxiappclient.utilities.VERSION
import io.reactivex.Single
import retrofit2.http.*

interface AuthClient {

    @GET("$VERSION/mobile/cities")
    fun getCities(): Single<List<CityInfo>>

    @POST("$VERSION/mobile/sign-in")
    @FormUrlEncoded
    fun signIn(
        @Field("phoneNumber") phoneNumber: String
    ): Single<SimpleResponse>

    @POST("$VERSION/mobile/verify-code")
    @FormUrlEncoded
    fun verifyPhoneNumber(
        @Field("phoneNumber") phoneNumber: String,
        @Field("code") code: String
    ): Single<VerificationResponse>

    @POST("$VERSION/mobile/update-info")
    @FormUrlEncoded
    fun updateUserInfo(
        @Header(TOKEN) token: String,
        @Field("city") city: City,
        @Field("name") name: String
    ): Single<SimpleResponse>
}