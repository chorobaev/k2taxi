package io.jachoteam.taxiappclient.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SharedPreferencesModule {

    @Provides
    @Named(TARIFF_PREFERENCES)
    fun provideTariffSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(TARIFF_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Named(ORDER_PREFERENCES)
    fun provideOrderSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(ORDER_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Named(LOCATIONS_PREFERENCES)
    fun provideLocationsPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(LOCATIONS_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Named(AUTH_PREFERENCES)
    fun providesAuthPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Named(USER_PREFERENCES)
    fun providesUserPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Named(DEVICE_INFO_PREFERENCES)
    fun providesDeviceInfoPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(DEVICE_INFO_PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        const val TARIFF_PREFERENCES = "io.jachoteam.taxiappclient.tariffs_prefs"
        const val ORDER_PREFERENCES = "io.jachoteam.taxiappclient.requests_prefs"
        const val LOCATIONS_PREFERENCES = "io.jachoteam.taxiappclient.locations_prefs"
        const val AUTH_PREFERENCES = "io.jachoteam.taxiappclient.auth_prefs"
        const val USER_PREFERENCES = "io.jachoteam.taxiappclient.user_prefs"
        const val DEVICE_INFO_PREFERENCES = "io.jachoteam.taxiappclient.device_info_prefs"
    }
}