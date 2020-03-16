package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule.Companion.USER_PREFERENCES
import io.jachoteam.taxiappclient.models.local.User
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor(
    @Named(USER_PREFERENCES)
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    private val _userLiveData = MutableLiveData<User>()

    val isUserRegistered: Boolean get() = user != null
    val userLiveData: LiveData<User> get() = _userLiveData

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                SAVED_USER -> _userLiveData.value = user
                else -> Unit
            }
        }

    init {
        _userLiveData.value = user
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    var user: User?
        set(value) {
            val userJson = gson.toJson(value)
            sharedPreferences.edit().putString(SAVED_USER, userJson).apply()
        }
        get() {
            val userJson = sharedPreferences.getString(SAVED_USER, null)
            return gson.fromJson(userJson, User::class.java)
        }

    fun clear() {
        user = null
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val SAVED_USER = "io.jachoteam.taxiappclient.saved_user"
    }
}