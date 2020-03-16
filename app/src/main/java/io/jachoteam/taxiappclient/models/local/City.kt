package io.jachoteam.taxiappclient.models.local

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import io.jachoteam.taxiappclient.R

enum class City {
    KYZYLKIYA,
    KADAMJAY;

    @get:StringRes
    val stringRes: Int get() = when(this) {
        KYZYLKIYA -> R.string.city_kyzylkiya
        KADAMJAY -> R.string.city_kadamjay
    }

    val latLng: LatLng get() = when(this) {
        KYZYLKIYA -> LatLng(40.261917, 72.132083)
        KADAMJAY -> LatLng(40.12833, 71.725449)
    }
}