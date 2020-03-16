package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule.Companion.DEVICE_INFO_PREFERENCES
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class DeviceInfoPreferences @Inject constructor(
    @Named(DEVICE_INFO_PREFERENCES)
    private val sharedPreferences: SharedPreferences
) {

    var versionCheckTimestamp: Long
        set(value) {
            sharedPreferences.edit().putLong(LAST_VERSION_CHECKED_TIME, value).apply()
        }
        get() {
            return sharedPreferences.getLong(LAST_VERSION_CHECKED_TIME, 0)
        }

    var savedVersionCode: Int
        set(value) {
            sharedPreferences.edit().putInt(SAVED_VERSION, value).apply()
        }
        get() {
            return sharedPreferences.getInt(SAVED_VERSION, NOT_EXIST)
        }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val NOT_EXIST = -1

        private const val LAST_VERSION_CHECKED_TIME =
            "io.jachoteam.taxiappclient.version_check_timestamp"
        private const val SAVED_VERSION = "io.jachoteam.taxiappclient.saved_version"
    }
}