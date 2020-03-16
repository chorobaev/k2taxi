package io.jachoteam.taxiappclient.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData
import javax.inject.Inject

class GpsStatusListener @Inject constructor(
        private val context: Context
) : LiveData<Boolean>() {

    val gpsEnabled: Boolean get() = context.isGpsEnabled()

    private val gpsSwitchStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = checkGpsAndReact()
    }

    override fun onInactive() = unregisterReceiver()

    override fun onActive() {
        registerReceiver()
        checkGpsAndReact()
    }

    private fun checkGpsAndReact() = postValue(context.isGpsEnabled())

    private fun Context.isGpsEnabled(): Boolean {
        return (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun unregisterReceiver() =
            context.unregisterReceiver(gpsSwitchStateReceiver)

    private fun registerReceiver() =
            context.registerReceiver(gpsSwitchStateReceiver,
                    IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
}