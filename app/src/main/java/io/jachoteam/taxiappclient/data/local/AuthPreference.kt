package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthPreference @Inject constructor(
    @Named(SharedPreferencesModule.AUTH_PREFERENCES)
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private val _authorized = MutableLiveData<Boolean>()
    val authorizedLiveData: LiveData<Boolean> get() = _authorized

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                AUTH_TOKEN -> _authorized.value = authToken != null
                else -> Unit
            }
        }

    init {
        _authorized.value = authToken != null
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    var authToken: String?
        set(value) {
            sharedPreferences.edit().putString(AUTH_TOKEN, value).apply()
        }
        get() = sharedPreferences.getString(AUTH_TOKEN, null)

    var phoneNumber: String?
        set(value) {
            sharedPreferences.edit().putString(PHONE_NUMBER, value).apply()

        }
        get() = sharedPreferences.getString(PHONE_NUMBER, "")

    var cityLatLng: LatLng
        set(value) {
            val json = gson.toJson(value)
            sharedPreferences.edit().putString(CITY_LATLNG, json).apply()
        }
        get() {
            val json = sharedPreferences.getString(CITY_LATLNG, null)
            return gson.fromJson(json, LatLng::class.java) ?: LatLng(40.2677, 72.127) // Kyzyl-Kyia
        }

    val isLoggedIn: Boolean get() = authToken != null

    var lastSmsSentTimestamp: Long
        set(value) {
            if (value > 0L) {
                sharedPreferences.edit().putLong(LAST_SMS_SENT_DATE, value).apply()
            } else {
                sharedPreferences.edit().remove(LAST_SMS_SENT_DATE).apply()
            }
        }
        get() {
            return sharedPreferences.getLong(LAST_SMS_SENT_DATE, 0)
        }

    fun logout() {
        authToken = null
        phoneNumber = null
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val AUTH_TOKEN = "io.jachoteam.taxiappclient.auth_token"
        private const val PHONE_NUMBER = "io.jachoteam.taxiappclient.phone_number"
        private const val CITY_LATLNG = "io.jachoteam.taxiappclient.city_latlng"
        private const val LAST_SMS_SENT_DATE = "io.jachoteam.taxiappclient.last_sms_sent_date"
    }
}