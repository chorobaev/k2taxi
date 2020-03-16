package io.jachoteam.taxiappclient.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainMapFragment(): MainMapFragment

    @ContributesAndroidInjector
    abstract fun bindOrderFragment(): OrderFragment
}