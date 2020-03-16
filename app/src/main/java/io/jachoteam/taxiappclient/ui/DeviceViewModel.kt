package io.jachoteam.taxiappclient.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jachoteam.taxiappclient.BuildConfig
import io.jachoteam.taxiappclient.base.BaseViewModel
import io.jachoteam.taxiappclient.data.repository.DeviceRepository
import io.jachoteam.taxiappclient.models.VersionChecker
import io.jachoteam.taxiappclient.models.local.VersionDifference
import javax.inject.Inject

class DeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val versionChecker: VersionChecker
) : BaseViewModel() {

    private val _versionStatus = MutableLiveData<VersionDifference>()

    val versionDifference: LiveData<VersionDifference> get() = _versionStatus

    fun checkVersionIfNeeded() {
        if (versionChecker.isTimeToCheckVersion(deviceRepository.lastVersionCheckedTimestamp)) {
            checkVersionWithNetwork()
        }
    }

    private fun checkVersionWithNetwork() {
        deviceRepository.appVersion.request { response ->
            _versionStatus.value =
                versionChecker.getDifferenceOfVersions(BuildConfig.VERSION_NAME, response.version)
            deviceRepository.saveVersionCheckedTimestamp()
        }
    }
}