package io.jachoteam.taxiappclient.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jachoteam.taxiappclient.di.module.SharedPreferencesModule.Companion.ORDER_PREFERENCES
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.roundToLong

@Singleton
class OrderPreference @Inject constructor(
    @Named(ORDER_PREFERENCES)
    private val sharedPreferences: SharedPreferences
) {
    private val _timer = MutableLiveData<Long>()
    private var javaTimer: Timer? = null

    init {
        checkRequests()
        resume()
        listenForTimer()
    }

    val timeUntilNextOrder: LiveData<Long> get() = _timer

    private val requests: Int get() = sharedPreferences.getInt(SENT_REQUESTS, 0)

    private val lastDate: Long get() = sharedPreferences.getLong(LAST_DATE, 0L)

    private val timeLeft: Long get() = lastDate + 1000 * EXPIRING_DURATION - Date().time

    fun addRequest() {
        checkRequests()
        sharedPreferences.edit().apply {
            val size = requests + 1
            if (size <= MAX_REQUESTS) {
                putLong(LAST_DATE, Date().time)
                putInt(SENT_REQUESTS, size)
            }
            if (size >= MAX_REQUESTS) {
                startTimer(EXPIRING_DURATION)
            }
        }.apply()
    }

    private fun checkRequests() {
        if (timeLeft <= 0) {
            sharedPreferences.edit().apply {
                remove(LAST_DATE)
                remove(SENT_REQUESTS)
            }.apply()
        }
    }

    private fun resume() {
        val left = timeLeft / 990.0
        if (left >= 0L) {
            startTimer(left.roundToLong())
        }
    }

    private fun listenForTimer() {
        timeUntilNextOrder.observeForever { count ->
            sharedPreferences.edit().apply {
                if (count == 0L) {
                    remove(SENT_REQUESTS)
                    remove(LAST_DATE)
                }
            }.apply()
        }
    }

    private fun startTimer(initialTime: Long) {
        javaTimer?.cancel()
        javaTimer = Timer()
        javaTimer?.schedule(object : TimerTask() {
            var counter = initialTime
            override fun run() {
                if (counter <= 0) {
                    cancel()
                }
                _timer.postValue(counter--)
            }
        }, 0, 1000)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val EXPIRING_DURATION = 20L
        private const val MAX_REQUESTS = 1
        private const val SENT_REQUESTS = "io.jachoteam.taxiappclient.requests"
        private const val LAST_DATE = "io.jachoteam.taxiappclient.last_date"
    }
}