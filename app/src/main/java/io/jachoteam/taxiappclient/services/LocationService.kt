package io.jachoteam.taxiappclient.services

import android.content.Intent
import android.location.Location
import android.os.IBinder
import io.jachoteam.taxiappclient.data.repository.LocationsRepository
import javax.inject.Inject

class LocationService : LocationTrackerService() {

    @Inject lateinit var locationsRepository: LocationsRepository

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDutyStatusChanged(permissionGranted: Boolean) {
        locationsRepository.changeDutyStatus(permissionGranted)
    }

    override fun onLocationChanged(location: Location) {
        locationsRepository.setLocation(location)
    }
}