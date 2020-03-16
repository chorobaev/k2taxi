package io.jachoteam.taxiappclient.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.jachoteam.taxiappclient.data.local.AuthPreference
import io.jachoteam.taxiappclient.data.network.AuthClient
import io.jachoteam.taxiappclient.data.network.DeviceClient
import io.jachoteam.taxiappclient.data.network.OrderClient
import io.jachoteam.taxiappclient.di.util.BaseUrl
import io.jachoteam.taxiappclient.di.util.K2Taxi
import io.jachoteam.taxiappclient.di.util.SMSVerification
import io.jachoteam.taxiappclient.models.TimeCounter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    AppConstantsModule::class,
    ActivityBindingModule::class,
    ServiceBindingModule::class,
    DialogModule::class,
    SharedPreferencesModule::class
])
class ApplicationModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    @K2Taxi
    fun provideK2TaxiRetrofit(
        @BaseUrl baseUrl: String,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit = retrofitBuilder.baseUrl(baseUrl).build()

    @Singleton
    @Provides
    fun provideOrderClient(@K2Taxi retrofit: Retrofit): OrderClient =
        retrofit.create(OrderClient::class.java)

    @Singleton
    @Provides
    fun provideAuthClient(@K2Taxi retrofit: Retrofit): AuthClient =
        retrofit.create(AuthClient::class.java)

    @Singleton
    @Provides
    fun provideDeviceClient(@K2Taxi retrofit: Retrofit): DeviceClient =
        retrofit.create(DeviceClient::class.java)

    @Provides
    @SMSVerification
    fun provideSMSVerificationTimeCounter(
        authPreference: AuthPreference,
        @SMSVerification waitingDurationInSeconds: Long
    ): TimeCounter {

        val timeSource =
            object : TimeCounter.TimeSource {
                override val startedTimestamp: Long
                    get() = authPreference.lastSmsSentTimestamp

                override fun saveStartedTimestamp(timestamp: Long) {
                    authPreference.lastSmsSentTimestamp = timestamp
                }
            }

        return TimeCounter(timeSource, waitingDurationInSeconds, true)
    }
}