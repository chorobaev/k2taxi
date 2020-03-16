package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule
import io.jachoteam.taxiappclient.models.local.Tariff
import io.jachoteam.taxiappclient.utilities.COMFORT
import io.jachoteam.taxiappclient.utilities.MINIVAN
import io.jachoteam.taxiappclient.utilities.PORTER
import io.jachoteam.taxiappclient.utilities.STANDARD
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TariffPreference @Inject constructor(
    @Named(SharedPreferencesModule.TARIFF_PREFERENCES)
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    private val _tariffsLiveData = MutableLiveData<List<Tariff>>()
    val tariffsLiveData: LiveData<List<Tariff>> get() = _tariffsLiveData

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                SAVED_TARIFFS -> _tariffsLiveData.value = tariffs
                else -> Unit
            }
        }

    init {
        _tariffsLiveData.value = tariffs
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    var tariffs: List<Tariff>
        set(value) {
            val tariffJson = gson.toJson(value)
            sharedPreferences.edit().putString(SAVED_TARIFFS, tariffJson).apply()
        }
        get() {
            val tariffsJson = sharedPreferences.getString(SAVED_TARIFFS, null)
            return if (tariffsJson.isNullOrEmpty()) {
                mockTariffs
            } else {
                val typeToken = object : TypeToken<List<Tariff>>() {}.type
                gson.fromJson(tariffsJson, typeToken)
            }
        }

    fun clear() {
        tariffs = emptyList()
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val SAVED_TARIFFS = "io.jachoteam.taxiappclient.saved_tariffs"

        private val mockTariffs = listOf(
            Tariff(-1, STANDARD, 35.0, 10.0),
            Tariff(-1, COMFORT, 55.0, 10.0),
            Tariff(-1, MINIVAN, 75.0, 10.0),
            Tariff(-1, PORTER, 260.0, 15.0)
        )
    }
}