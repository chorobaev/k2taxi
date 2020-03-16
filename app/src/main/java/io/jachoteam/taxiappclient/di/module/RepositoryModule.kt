package io.jachoteam.taxiappclient.di.module

import dagger.Module
import dagger.Provides
import io.jachoteam.taxiappclient.data.local.*
import io.jachoteam.taxiappclient.data.network.AuthClient
import io.jachoteam.taxiappclient.data.network.DeviceClient
import io.jachoteam.taxiappclient.data.network.OrderClient
import io.jachoteam.taxiappclient.data.repository.*
import io.jachoteam.taxiappclient.data.repository.implementation.*
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesOrderRepository(
        userClient: OrderClient,
        tariffUtility: TariffPreference,
        orderPreference: OrderPreference,
        authPreference: AuthPreference
    ): OrderRepository =
        OrderRepositoryImpl(userClient, tariffUtility, orderPreference, authPreference)

    @Singleton
    @Provides
    fun providesAuthRepository(
        authClient: AuthClient,
        authPreference: AuthPreference,
        locationsPreference: LocationsPreference,
        orderPreference: OrderPreference,
        tariffPreference: TariffPreference,
        userPreference: UserPreference
    ): AuthRepository =
        AuthRepositoryImpl(
            authClient,
            authPreference,
            locationsPreference,
            orderPreference,
            tariffPreference,
            userPreference
        )

    @Singleton
    @Provides
    fun providesLocationsRepository(
        locationsPreference: LocationsPreference
    ): LocationsRepository =
        LocationsRepositoryImpl(locationsPreference)

    @Singleton
    @Provides
    fun providesDeviceRepository(
        authPreference: AuthPreference,
        deviceClient: DeviceClient,
        deviceInfoPreferences: DeviceInfoPreferences
    ): DeviceRepository =
        DeviceRepositoryImpl(deviceClient, authPreference, deviceInfoPreferences)

    @Singleton
    @Provides
    fun provideUserRepository(
        userPreference: UserPreference
    ): UserRepository =
        UserRepositoryImpl(userPreference)
}