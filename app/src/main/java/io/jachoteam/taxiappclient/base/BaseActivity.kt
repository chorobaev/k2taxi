package io.jachoteam.taxiappclient.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.jachoteam.taxiappclient.ui.auth.AuthActivity
import io.jachoteam.taxiappclient.ui.main.MainActivity
import io.jachoteam.taxiappclient.ui.splash.SplashActivity
import io.jachoteam.taxiappclient.utilities.GpsRequiredDialog
import io.jachoteam.taxiappclient.utilities.GpsStatusListener
import io.jachoteam.taxiappclient.utilities.locationPermissionsGranted
import io.jachoteam.taxiappclient.utilities.makeShortToast
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject lateinit var gpsStatusListener: GpsStatusListener
    @Inject lateinit var gpsRequiredDialog: GpsRequiredDialog
    @Inject lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var commonViewModel: CommonViewModel
    private var errorListener: ((String) -> Unit)? = null

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected open fun onInitUI(firstInit: Boolean) {}
    protected open fun onInitViewModel() {}
    protected open fun onObserveViewModel() {}
    protected open fun onSetOnClickListeners() {}

    protected open fun onGpsEnabled() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        if (this !is SplashActivity && this !is AuthActivity) {
            observeGpsStatus()
        }

        observeCommonViewModel()
        onInitViewModel()
        onInitUI(savedInstanceState == null)
        onObserveViewModel()
        onSetOnClickListeners()
    }

    protected open fun observeGpsStatus() {
        if (locationPermissionsGranted) {
            gpsStatusListener.observe(this, Observer { enabled ->
                enabled?.let {
                    if (!enabled && !gpsRequiredDialog.isAdded) {
                        showGpsRequiredDialog()
                    } else {
                        onGpsEnabled()
                    }
                }
            })
        }
    }

    protected fun setErrorListener(block: ((String) -> Unit)?) {
        errorListener = block
    }

    private fun showGpsRequiredDialog() {
        gpsRequiredDialog.setPositiveClickedRunnable(Runnable {
            if (gpsStatusListener.gpsEnabled) {
                onGpsEnabled()
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        })

        gpsRequiredDialog.setNegativeClickedRunnable(Runnable { this.finish() })
        gpsRequiredDialog.show(supportFragmentManager, "GPS unavailable")
    }

    private fun observeCommonViewModel() {
        commonViewModel = ViewModelProvider(this, viewModelFactory)[CommonViewModel::class.java]

        commonViewModel.error.observe(this, Observer {
            it?.let {
                val listener = errorListener
                if (listener == null) {
                    makeShortToast(it)
                } else {
                    listener.invoke(it)
                }
            }
        })
        commonViewModel.unauthorized.observe(this, Observer {
            startAuthActivity()
            stopMainActivity()
        })
    }

    protected fun startAuthActivity() {
        if (this is AuthActivity) return
        Intent(this, AuthActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            startActivity(it)
            finish()
        }
    }

    protected fun startMainActivity(extras: Bundle? = null) {
        if (this is MainActivity) return
        Intent(this, MainActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            if (extras != null) it.putExtras(extras)
            startActivity(it)
            finish()
        }
    }

    protected fun stopMainActivity() {
        Intent(MainActivity.ACTION_FINISH_MAIN_ACTIVITY).also {
            sendBroadcast(it)
        }
    }

    protected inline fun <reified VM : BaseViewModel> getViewModel(): VM =
        ViewModelProvider(this, viewModelFactory)[VM::class.java].also {
            it.setOnErrorListener(commonViewModel)
        }

    companion object {
        private const val DOUBLE_TAP_EXIT_DURATION = 2000L
    }
}