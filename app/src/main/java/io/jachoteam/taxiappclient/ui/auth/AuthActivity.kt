package io.jachoteam.taxiappclient.ui.auth

import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseActivity
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.utilities.ProgressDialog

class AuthActivity : BaseActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var progressDialog: ProgressDialog

    override val layoutRes: Int get() = R.layout.activity_auth

    override fun onInitUI(firstInit: Boolean) {
        progressDialog = ProgressDialog(this)

        if (firstInit) {
            if (authViewModel.isPhoneNumberVerified) {
                showFragmentOnActivity(RegistrationFragment())
            } else {
                showFragmentOnActivity(LoginFragment())
            }
        }
    }

    override fun onInitViewModel() {
        authViewModel = getViewModel()
    }

    override fun onObserveViewModel() {

        authViewModel.loading.observe(this, Observer { loading ->
            if (loading == true) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        authViewModel.shouldGoVerifyFragment.observe(this, Observer {
            showFragmentOnActivity(VerifyFragment(), backStackEnabled = true)
        })

        authViewModel.shouldGoRegisterFragment.observe(this, Observer {
            supportFragmentManager.popBackStack()
            showFragmentOnActivity(RegistrationFragment())
        })

        authViewModel.authenticationSuccess.observe(this, Observer {
            startMainActivity()
        })
    }

    private fun showFragmentOnActivity(fragment: BaseFragment, backStackEnabled: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()

        if (backStackEnabled) {
            transaction.addToBackStack(this::class.java.simpleName)
        }

        transaction.replace(R.id.fragment_container, fragment).commit()
    }

    override fun onDestroy() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroy()
    }
}
