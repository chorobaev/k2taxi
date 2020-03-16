package io.jachoteam.taxiappclient.ui.profile

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.local.User
import io.jachoteam.taxiappclient.models.network.CityInfo
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override val layoutRes: Int get() = R.layout.fragment_profile_edit

    override fun onInitViewModel() {
        profileViewModel = getViewModel()
        profileViewModel.requestCities()
    }

    override fun onInitUI(firstInit: Boolean) {
        et_nickname.addTextChangedListener {
            enableSaveButtonIfPossible()
        }

        city_group.setOnCityChangedListener {
            enableSaveButtonIfPossible()
        }
    }

    override fun onObserveViewModel() {
        profileViewModel.userLiveData.observe(this, Observer {
            it?.let { onUpdateUserInfo(it) }
        })

        profileViewModel.cities.observe(this, Observer {
            it?.let { onCitiesReceived(it) }
        })
    }

    private fun onUpdateUserInfo(user: User) {
        user.name?.let {
            et_nickname.setText(it)
        }
        focusOnEditText()
    }

    private fun focusOnEditText() {
        et_nickname.run {
            requestFocus()
            setSelection(text?.length ?: 0)
        }
    }

    private fun onCitiesReceived(cities: List<CityInfo>) {
        city_group.run {
            clear()
            addCities(cities)
        }
    }

    private fun enableSaveButtonIfPossible() {
        btn_save.isEnabled =
            et_nickname.text?.length ?: 0 >= 3
    }

    override fun onSetOnClickListeners() {
        btn_save.setOnClickListener {
            profileViewModel.updateUserInformation(
                name = et_nickname.text.toString(),
                city = city_group.getSelectedCity().tag
            )
        }
    }
}