package io.jachoteam.taxiappclient.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseActivity
import io.jachoteam.taxiappclient.di.util.AppVersionDialog
import io.jachoteam.taxiappclient.di.util.LogoutConfirmDialog
import io.jachoteam.taxiappclient.models.local.City
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.models.local.VersionDifference
import io.jachoteam.taxiappclient.ui.DeviceViewModel
import io.jachoteam.taxiappclient.ui.profile.ProfileActivity
import io.jachoteam.taxiappclient.utilities.ConfirmDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    @field:AppVersionDialog
    lateinit var appVersionDialog: ConfirmDialog

    @Inject
    @field:LogoutConfirmDialog
    lateinit var logoutConfirmDialog: ConfirmDialog

    private lateinit var viewModel: MainViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var tvUserName: TextView
    private var currentCity: City? = null

    override val layoutRes: Int get() = R.layout.activity_main

    override fun onInitUI(firstInit: Boolean) {
        setErrorListener {
            app_notification.showErrorNotification(it)
        }

        initNavigationDrawer()
        initTitle()
        initAppVersionDialog()
        initLogoutConfirmDialog()
    }

    private fun initNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        tvUserName = nav_view.getHeaderView(0).tv_user_name
    }

    @Suppress("DEPRECATION")
    private fun initTitle() {
        val title = String.format(TITLE, getString(R.string.app_name_taxi))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_title.text = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY)
        } else {
            tv_title.text = Html.fromHtml(title)
        }
    }

    private fun initAppVersionDialog() {
        appVersionDialog.setPositiveClickedRunnable(Runnable {
            openPlayStore()
        })

        appVersionDialog.setNegativeClickedRunnable(Runnable {
            when (deviceViewModel.versionDifference.value) {
                VersionDifference.MAJOR -> finish()
                else -> Unit
            }
        })
    }

    private fun initLogoutConfirmDialog() {
        logoutConfirmDialog.setPositiveClickedRunnable(Runnable {
            logout()
        })

        logoutConfirmDialog.setNegativeClickedRunnable(Runnable {

        })
    }

    private fun logout() {
        viewModel.logout()
        startAuthActivity()
    }

    private fun openPlayStore() {
        try {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            ).also {
                startActivity(it)
            }
        } catch (anfe: ActivityNotFoundException) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            ).also {
                startActivity(it)
            }
        }
    }

    override fun onInitViewModel() {
        viewModel = getViewModel()
        viewModel.requestTariffs()
        viewModel.requestDrivers()

        deviceViewModel = getViewModel()
    }

    override fun onStart() {
        super.onStart()
        deviceViewModel.checkVersionIfNeeded()
    }

    override fun onObserveViewModel() {
        viewModel.userLiveData.observe(this, Observer {
            it?.let { onUpdateUserInfo(it) }
        })

        viewModel.notification.observe(this, Observer {
            it?.let { app_notification.showNotification(it) }
        })

        deviceViewModel.versionDifference.observe(this, Observer {
            it?.let { onVersionDifferenceChanged(it) }
        })
    }

    private fun onUpdateUserInfo(user: User) {
        if (currentCity != null && currentCity != user.city) {
            viewModel.requestDrivers()
            user.city?.let { viewModel.moveToLocation(it.latLng) }
        }

        user.name?.let {
            tvUserName.text = it
        }
        currentCity = user.city
    }

    private fun onVersionDifferenceChanged(difference: VersionDifference) {
        iv_new_version.visibility = if (difference == VersionDifference.NONE) GONE else VISIBLE
        if (difference == VersionDifference.MAJOR) {
            showAppVersionDialog()
        }
    }

    private fun showAppVersionDialog() {
        if (!appVersionDialog.isAdded) {
            appVersionDialog.show(supportFragmentManager, ConfirmDialog::class.java.simpleName)
        }
    }

    override fun onSetOnClickListeners() {
        iv_new_version.setOnClickListener {
            showAppVersionDialog()
        }

        nav_view.getHeaderView(0).setOnClickListener {
            startProfileActivity()
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun startProfileActivity() {
        Intent(this, ProfileActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_logout -> showLogoutConfirmDialog()
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return false
    }

    private fun showLogoutConfirmDialog() {
        logoutConfirmDialog.show(supportFragmentManager, "logout")
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val ACTION_FINISH_MAIN_ACTIVITY = "io.jachoteam.taxiappclient.finish_main_activity"
        private const val TITLE = "<font color='red'>K</font><font color='black'>2 %s</font>"
    }
}
