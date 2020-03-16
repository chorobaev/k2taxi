package io.jachoteam.taxiappclient.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.jachoteam.taxiappclient.base.CommonViewModel
import io.jachoteam.taxiappclient.base.ViewModelFactory
import io.jachoteam.taxiappclient.di.util.ViewModelKey
import io.jachoteam.taxiappclient.ui.DeviceViewModel
import io.jachoteam.taxiappclient.ui.auth.AuthViewModel
import io.jachoteam.taxiappclient.ui.main.MainViewModel
import io.jachoteam.taxiappclient.ui.profile.ProfileViewModel
import io.jachoteam.taxiappclient.ui.splash.SplashViewModel


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CommonViewModel::class)
    abstract fun bindCommonViewModel(commonViewModel: CommonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeviceViewModel::class)
    abstract fun bindDeviceViewModel(deviceViewModel: DeviceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}