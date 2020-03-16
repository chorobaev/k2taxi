package io.jachoteam.taxiappclient.ui.main

import android.location.Location
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.local.Driver
import io.jachoteam.taxiappclient.models.local.latLng
import io.jachoteam.taxiappclient.utilities.buildTaxiMarker
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels

class MainMapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var viewModel: MainViewModel
    private var location: Location? = null
    private var map: GoogleMap? = null
    private var markerDrivers = ArrayList<Marker?>()

    override val layoutRes: Int get() = R.layout.fragment_main_map

    override fun onInitViewModel() {
        viewModel = getViewModel()
    }

    override fun onInitUI(firstInit: Boolean) {
        requireMap()
        viewModel.requestDrivers()
    }

    private fun requireMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap?.apply {

            isMyLocationEnabled = true
            uiSettings.isZoomControlsEnabled = true

            setMinZoomPreference(MIN_ZOOM)
            setMaxZoomPreference(MAX_ZOOM)

            val padding = 52.inDensity.toPixels(resources.displayMetrics).toInt()
            setPadding(0, padding, 0, 0)

            moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.cityLatLng, CAMERA_ZOOM_LEVEL))

            val kg = LatLngBounds(
                LatLng(39.149249, 69.250350),
                LatLng(43.282504, 80.234691)
            )
            setLatLngBoundsForCameraTarget(kg)
        }
        subscribeToDrivers()
    }

    private fun subscribeToDrivers() {
        viewModel.drivers.observe(this, Observer {
            it?.let {
                removePreviousDrivers()
                drawDriversOnMap(it)
            }
        })
    }

    private fun drawDriversOnMap(drivers: List<Driver>) {
        drivers.forEach { driver ->
            baseActivity.buildTaxiMarker(driver.latLng)
                .also {
                    val marker = map?.addMarker(it)
                    markerDrivers.add(marker)
                }
        }
    }

    private fun removePreviousDrivers() {
        markerDrivers.forEach {
            it?.remove()
        }
    }

    override fun onObserveViewModel() {
        viewModel.shouldMoveToLocation.observe(this, Observer {
            it?.let { updateCameraPosition(it) }
        })

        viewModel.location.observe(this, Observer {
            location = it
        })
    }

    private fun updateCameraPosition(latLng: LatLng) {
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(CAMERA_ZOOM_LEVEL)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {
        private const val CAMERA_ZOOM_LEVEL = 15f
        private const val MIN_ZOOM = 5.5F
        private const val MAX_ZOOM = 21F
    }
}