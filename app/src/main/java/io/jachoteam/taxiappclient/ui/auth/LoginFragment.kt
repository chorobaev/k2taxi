package io.jachoteam.taxiappclient.ui.auth

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.TimeCounter
import io.jachoteam.taxiappclient.utilities.PhoneNumberVerificationDialog
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var verifyDialog: PhoneNumberVerificationDialog

    override val layoutRes: Int get() = R.layout.fragment_login

    override fun onInitViewModel() {
        authViewModel = getViewModel()
    }

    override fun onInitUI(firstInit: Boolean) {
        verifyDialog = PhoneNumberVerificationDialog(baseActivity)

        verifyDialog.setOnEditClickedListener {
            et_phone_number.requestFocus()
        }

        verifyDialog.setOnOKClickedListener {
            authViewModel.signIn(getPhoneNumber())
        }

        et_phone_number.addTextChangedListener {
            enableNextButtonIfPossible()
        }
    }

    override fun onObserveViewModel() {
        authViewModel.timeToResendCode.observe(this, Observer {
            it?.let { onSMSVerificationTimeReceived(it) }
        })
    }

    private fun onSMSVerificationTimeReceived(time: TimeCounter.Time) {
        if (time.isCompleted) {
            enableNextButtonIfPossible()
            tv_resend_timer.visibility = INVISIBLE
        } else {
            tv_resend_timer.visibility = VISIBLE
            tv_resend_timer.text = getString(
                R.string.auth_msg_resend_verification_code_in_long_seconds,
                time.timeInSeconds
            )
        }
    }

    private fun enableNextButtonIfPossible() {
        btn_next.isEnabled =
            et_phone_number.text?.length ?: 0 >= 9 &&
                authViewModel.timeToResendCode.value?.isCompleted == true
    }

    override fun onSetOnClickListeners() {
        btn_next.setOnClickListener {
            if (context != null) {
                verifyDialog.show(
                    getString(R.string.country_code_string, getPhoneNumber())
                )
            }
        }
    }

    private fun getPhoneNumber(): String =
        et_phone_number.text.toString()
}