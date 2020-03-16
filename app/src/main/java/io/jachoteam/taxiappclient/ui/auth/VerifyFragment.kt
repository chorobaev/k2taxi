package io.jachoteam.taxiappclient.ui.auth

import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.TimeCounter
import kotlinx.android.synthetic.main.fragment_verify.*

class VerifyFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel

    override val layoutRes: Int get() = R.layout.fragment_verify

    override fun onInitUI(firstInit: Boolean) {
        code_input_view.setOnCodeChangedListener {
            btn_verify.isEnabled = it
            if (it) {
                verifyCode()
            }
        }

        code_resending_view.setOnResendListener {
            authViewModel.run {
                signIn(phoneNumber.value!!, shouldStartVerifyFragment = false)
            }
        }
    }

    override fun onInitViewModel() {
        authViewModel = getViewModel()
    }

    override fun onObserveViewModel() {
        authViewModel.phoneNumber.observe(this, Observer { phoneNumber ->
            phoneNumber?.let {
                tv_code_sent_phone_number.text = getString(R.string.country_code_string, it)
            }
        })

        authViewModel.timeToResendCode.observe(this, Observer {
            it?.let { onSMSVerificationTimeReceived(it) }
        })
    }

    private fun onSMSVerificationTimeReceived(time: TimeCounter.Time) {
        code_resending_view.isCompleted = time.isCompleted
        if (!time.isCompleted) {
            code_resending_view.time = time.timeInSeconds
        }
    }

    override fun onSetOnClickListeners() {
        btn_verify.setOnClickListener {
            verifyCode()
        }
    }

    private fun verifyCode() {
        authViewModel.verifySMSCode(code_input_view.code)
    }
}