package io.jachoteam.taxiappclient.data.repository

import androidx.lifecycle.LiveData
import io.jachoteam.taxiappclient.models.local.User

interface UserRepository {

    val userLiveData: LiveData<User>
}