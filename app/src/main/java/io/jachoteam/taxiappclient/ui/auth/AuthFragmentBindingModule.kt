package io.jachoteam.taxiappclient.ui.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun bindVerifyFragment(): VerifyFragment

    @ContributesAndroidInjector
    abstract fun bindRegistrationFragment(): RegistrationFragment
}