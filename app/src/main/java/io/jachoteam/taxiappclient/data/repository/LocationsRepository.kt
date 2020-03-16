package io.jachoteam.taxiappclient.data.repository

import android.location.Location

interface LocationsRepository : LocationEmitter {

    fun setLocation(location: Location)
}