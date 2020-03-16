package io.jachoteam.taxiappclient.ui.profile

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindProflieViewFragment(): ProfileViewFragment

    @ContributesAndroidInjector
    abstract fun bindProfileEditFragment(): ProfileEditFragment
}