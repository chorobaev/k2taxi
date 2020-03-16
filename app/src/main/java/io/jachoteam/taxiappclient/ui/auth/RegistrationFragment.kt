package io.jachoteam.taxiappclient.ui.auth

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.network.CityInfo
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel

    override val layoutRes: Int get() = R.layout.fragment_registration

    override fun onInitViewModel() {
        authViewModel = getViewModel()
        authViewModel.requestCities()
    }

    override fun onInitUI(firstInit: Boolean) {
        et_nickname.addTextChangedListener {
            enableRegisterButtonIfPossible()
        }

        city_group.setOnCityChangedListener {
            enableRegisterButtonIfPossible()
        }
    }

    private fun enableRegisterButtonIfPossible() {
        btn_register.isEnabled =
            et_nickname.text?.length ?: 0 >= 3
    }

    override fun onObserveViewModel() {
        authViewModel.cities.observe(this, Observer {
            it?.let { onCitiesReceived(it) }
        })
    }

    private fun onCitiesReceived(cities: List<CityInfo>) {
        city_group.run {
            clear()
            addCities(cities)
        }
    }

    override fun onSetOnClickListeners() {
        btn_register.setOnClickListener {
            authViewModel.updateUserInformation(
                name = et_nickname.text.toString(),
                city = city_group.getSelectedCity().tag
            )
        }
    }
}