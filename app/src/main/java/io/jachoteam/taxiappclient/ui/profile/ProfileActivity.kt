package io.jachoteam.taxiappclient.ui.profile

import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseActivity
import io.jachoteam.taxiappclient.utilities.ProgressDialog
import io.jachoteam.taxiappclient.utilities.makeShortToast
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var progressDialog: ProgressDialog

    override val layoutRes: Int get() = R.layout.activity_profile

    override fun onInitViewModel() {
        profileViewModel = getViewModel()
    }

    override fun onInitUI(firstInit: Boolean) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)

        if (firstInit) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ProfileViewFragment())
                .commit()
        }
    }

    override fun onObserveViewModel() {

        profileViewModel.loading.observe(this, Observer { loading ->
            if (loading == true) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        profileViewModel.shouldShowProfileEditFragment.observe(this, Observer {
            showProfileEditFragment()
        })

        profileViewModel.shouldCloseProfileEditFragment.observe(this, Observer {
            showSavedMessage()
            onBackPressed()
        })
    }

    private fun showProfileEditFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .addToBackStack(ProfileEditFragment::class.java.simpleName)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container, ProfileEditFragment())
            .commit()
    }

    private fun showSavedMessage() {
        makeShortToast(R.string.profile_msg_saved)
    }

    override fun onDestroy() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
