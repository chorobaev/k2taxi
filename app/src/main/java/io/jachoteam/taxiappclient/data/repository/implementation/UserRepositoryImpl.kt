package io.jachoteam.taxiappclient.data.repository.implementation

import androidx.lifecycle.LiveData
import io.jachoteam.taxiappclient.data.local.UserPreference
import io.jachoteam.taxiappclient.data.repository.UserRepository
import io.jachoteam.taxiappclient.models.local.User

class UserRepositoryImpl(
    private val userPreference: UserPreference
) : UserRepository {

    override val userLiveData: LiveData<User> get() = userPreference.userLiveData
}