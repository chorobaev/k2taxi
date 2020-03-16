package io.jachoteam.taxiappclient.ui.splash

import android.content.Intent
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseActivity
import io.jachoteam.taxiappclient.services.LocationService
import io.jachoteam.taxiappclient.ui.main.MainActivity
import io.jachoteam.taxiappclient.utilities.LOCATION_PERMISSION_CODE
import io.jachoteam.taxiappclient.utilities.PermissionDeniedDialog
import io.jachoteam.taxiappclient.utilities.locationPermissionsGranted
import io.jachoteam.taxiappclient.utilities.requestLocationPermissions
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject lateinit var locationPermissionDeniedDialog: PermissionDeniedDialog
    private lateinit var splashViewModel: SplashViewModel

    override val layoutRes: Int get() = R.layout.activity_splash

    override fun onInitViewModel() {
        splashViewModel = getViewModel()
    }

    override fun onInitUI(firstInit: Boolean) {
        if (!locationPermissionsGranted) {
            requestLocationPermissions()
        } else {
            onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val mFineGranted =
            requestCode == LOCATION_PERMISSION_CODE && locationPermissionsGranted

        if (mFineGranted) {
            onPermissionGranted()
        } else {
            showLocationPermissionDeniedDialog()
        }
    }

    private fun onPermissionGranted() {
        startLocationService()
        startActivities()
    }

    private fun startLocationService() {
        Intent(this, LocationService::class.java).also {
            startService(it)
        }
    }

    private fun showLocationPermissionDeniedDialog() {
        locationPermissionDeniedDialog.show(supportFragmentManager, "Permission denied")
    }

    private fun startActivities() {
        if (!splashViewModel.isUserRegistered) {
            startAuthActivity()
        } else {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}