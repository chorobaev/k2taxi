package io.jachoteam.taxiappclient.services

import android.location.Location
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import io.jachoteam.taxiappclient.base.BaseService
import io.jachoteam.taxiappclient.utilities.locationPermissionsGranted

abstract class LocationTrackerService : BaseService(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val mLocationCallback = LocationTrackerCallback()

    abstract override fun onLocationChanged(location: Location)

    protected abstract fun onDutyStatusChanged(permissionGranted: Boolean)

    override fun onCreate() {
        super.onCreate()
        initGoogleApiClient()
        if (mGoogleApiClient != null && !mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.connect()
        }
    }

    @Synchronized
    protected fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationUpdate()
    }

    protected fun startLocationUpdate() {
        checkLocationPermission()
        setLocationRequestParams()
        if (locationPermissionsGranted) {
            mFusedLocationProviderClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        }
    }

    private fun setLocationRequestParams() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL.toLong()
        mLocationRequest.smallestDisplacement = SMALLEST_DISPLACEMENT_METERS
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onDestroy() {
        if (mGoogleApiClient != null) {
            stopRequestingUpdates()
        }
        super.onDestroy()
    }

    protected fun stopRequestingUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    protected fun checkLocationPermission() {
        val granted = locationPermissionsGranted
        onDutyStatusChanged(granted)
    }

    private fun turnDutyStatus(isAllowed: Boolean) {
        onDutyStatusChanged(isAllowed)
    }

    private inner class LocationTrackerCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            onLocationChanged(locationResult!!.lastLocation)
        }
    }

    companion object {
        private const val UPDATE_INTERVAL = 2000
        private const val FASTEST_UPDATE_INTERVAL = 2000
        private const val SMALLEST_DISPLACEMENT_METERS = 6f
    }
}