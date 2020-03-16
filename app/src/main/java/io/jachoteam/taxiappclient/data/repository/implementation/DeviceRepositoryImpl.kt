package io.jachoteam.taxiappclient.data.repository.implementation

import io.jachoteam.taxiappclient.BuildConfig
import io.jachoteam.taxiappclient.data.local.AuthPreference
import io.jachoteam.taxiappclient.data.local.DeviceInfoPreferences
import io.jachoteam.taxiappclient.data.network.DeviceClient
import io.jachoteam.taxiappclient.data.repository.DeviceRepository
import io.jachoteam.taxiappclient.models.network.VersionResponse
import io.reactivex.Single
import java.util.*

class DeviceRepositoryImpl(
    private val deviceClient: DeviceClient,
    private val authPreference: AuthPreference,
    private val deviceInfoPreferences: DeviceInfoPreferences
) : DeviceRepository {

    private val token: String get() = authPreference.authToken ?: ""

    override val appVersion: Single<VersionResponse>
        get() = deviceClient.getAppVersion(token)

    override val lastVersionCheckedTimestamp: Long
        get() = deviceInfoPreferences.versionCheckTimestamp

    override val isInitialInstallation: Boolean
        get() {
            val isInitial = deviceInfoPreferences.savedVersionCode == DeviceInfoPreferences.NOT_EXIST
            deviceInfoPreferences.savedVersionCode = BuildConfig.VERSION_CODE
            return isInitial
        }

    override fun saveVersionCheckedTimestamp() {
        deviceInfoPreferences.versionCheckTimestamp = Date().time
    }
}