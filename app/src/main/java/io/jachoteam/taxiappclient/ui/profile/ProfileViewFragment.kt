package io.jachoteam.taxiappclient.ui.profile

import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.local.User
import kotlinx.android.synthetic.main.fragment_profile_view.*

class ProfileViewFragment : BaseFragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override val layoutRes: Int get() = R.layout.fragment_profile_view

    override fun onInitViewModel() {
        profileViewModel = getViewModel()
    }

    override fun onSetOnClickListeners() {
        btn_edit.setOnClickListener {
            profileViewModel.openProfileEditFragment()
        }
    }

    override fun onObserveViewModel() {
        profileViewModel.userLiveData.observe(this, Observer {
            it?.let { onUpdateUserInfo(it) }
        })
    }

    private fun onUpdateUserInfo(user: User) {
        user.name?.let {
            et_nickname.setText(it)
        }
        user.city?.let {
            et_city.setText(it.stringRes)
        }
    }
}