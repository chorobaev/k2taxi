package io.jachoteam.taxiappclient.data.repository

import io.jachoteam.taxiappclient.models.network.VersionResponse
import io.reactivex.Single

interface DeviceRepository {

    val appVersion: Single<VersionResponse>

    val lastVersionCheckedTimestamp: Long

    val isInitialInstallation: Boolean

    fun saveVersionCheckedTimestamp()
}