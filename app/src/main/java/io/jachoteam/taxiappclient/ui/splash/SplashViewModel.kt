package io.jachoteam.taxiappclient.ui.splash

import io.jachoteam.taxiappclient.base.BaseViewModel
import io.jachoteam.taxiappclient.data.repository.AuthRepository
import io.jachoteam.taxiappclient.data.repository.DeviceRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    init {
        checkInitialInstallation()
    }

    private fun checkInitialInstallation() {
        if (deviceRepository.isInitialInstallation) {
            authRepository.logout()
        }
    }

    val isUserRegistered: Boolean get() = authRepository.isUserRegistered()
}