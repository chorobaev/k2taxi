package io.jachoteam.taxiappclient.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.jachoteam.taxiappclient.services.LocationService

@Module
abstract class ServiceBindingModule {

    @ContributesAndroidInjector
    abstract fun bindLocationService(): LocationService
}