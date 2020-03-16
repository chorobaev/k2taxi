package io.jachoteam.taxiappclient.data.repository

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationEmitter {

    val location: LiveData<Location>

    fun changeDutyStatus(enabled: Boolean)
}