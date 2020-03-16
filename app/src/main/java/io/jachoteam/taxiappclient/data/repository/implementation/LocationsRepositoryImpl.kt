package io.jachoteam.taxiappclient.data.repository.implementation

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jachoteam.taxiappclient.data.local.LocationsPreference
import io.jachoteam.taxiappclient.data.repository.LocationsRepository

class LocationsRepositoryImpl(
    private val locationsPreference: LocationsPreference
) : LocationsRepository {

    private val _locationsEnabled = MutableLiveData<Boolean>()
    val locationsEnabled: LiveData<Boolean> get() = _locationsEnabled

    override val location: LiveData<Location>
        get() = locationsPreference.location

    override fun setLocation(location: Location) {
        locationsPreference.savedLocation = location
    }

    override fun changeDutyStatus(enabled: Boolean) {
        if (_locationsEnabled.value != enabled) {
            _locationsEnabled.value = enabled
        }
    }
}