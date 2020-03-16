package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule.Companion.LOCATIONS_PREFERENCES
import io.jachoteam.taxiappclient.models.local.K2Location
import io.jachoteam.taxiappclient.models.local.toK2Location
import io.jachoteam.taxiappclient.models.local.toLocation
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocationsPreference @Inject constructor(
    @Named(LOCATIONS_PREFERENCES)
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                SAVED_LOCATION -> _location.value = savedLocation
                else -> Unit
            }
        }

    init {
        _location.value = savedLocation
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    var savedLocation: Location?
        set(value) {
            val json = gson.toJson(value?.toK2Location())
            sharedPreferences.edit().putString(SAVED_LOCATION, json).apply()
        }
        get() {
            val json = sharedPreferences.getString(SAVED_LOCATION, null)
            return gson.fromJson(json, K2Location::class.java)?.toLocation()
        }

    fun clear() {
        savedLocation = null
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val SAVED_LOCATION = "io.jachoteam.taxiappclient.saved_location"
    }
}