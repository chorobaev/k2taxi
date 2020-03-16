package io.jachoteam.taxiappclient.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.jachoteam.taxiappclient.ui.auth.AuthActivity
import io.jachoteam.taxiappclient.ui.auth.AuthFragmentBindingModule
import io.jachoteam.taxiappclient.ui.main.MainActivity
import io.jachoteam.taxiappclient.ui.main.MainFragmentBindingModule
import io.jachoteam.taxiappclient.ui.profile.ProfileActivity
import io.jachoteam.taxiappclient.ui.profile.ProfileFragmentBindingModule
import io.jachoteam.taxiappclient.ui.splash.SplashActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [MainFragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [AuthFragmentBindingModule::class])
    abstract fun bindAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [ProfileFragmentBindingModule::class])
    abstract fun bindProfileActivity(): ProfileActivity
}