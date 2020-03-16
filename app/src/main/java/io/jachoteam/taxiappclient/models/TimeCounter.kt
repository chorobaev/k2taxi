package io.jachoteam.taxiappclient.models

import androidx.lifecycle.LiveData
import java.util.*
import kotlin.math.roundToLong

class TimeCounter(
    private val timeSource: TimeSource,
    private val durationInSec: Long,
    private val countDown: Boolean
) : LiveData<TimeCounter.Time>() {

    private var javaTimer: Timer? = null
    private var started = false

    init {
        value = Time(true, 0)
        // Timer was started if difference of current and saved time is greater than the specified ms
        started = timeSource.startedTimestamp > 0
    }

    fun start() {
        stop()
        timeSource.saveStartedTimestamp(Date().time)
        started = true
        startTimer()
    }

    override fun onActive() {
        super.onActive()
        if (started) {
            startTimer()
        }
    }

    override fun onInactive() {
        super.onInactive()
        stopTimer()
    }

    fun stop() {
        stopTimer()
        timeSource.saveStartedTimestamp(0)
        started = false
    }

    private fun startTimer() {
        val passedTime = Date().time - timeSource.startedTimestamp

        if (hasActiveObservers()) {
            startTimer((passedTime / 990.0).roundToLong())
        }
    }

    private fun startTimer(initialTime: Long) {
        javaTimer?.cancel()
        javaTimer = Timer()
        javaTimer?.schedule(object : TimerTask() {
            var counter = initialTime
            override fun run() {
                val nextSec = if (countDown) durationInSec - counter else counter
                val nextValue = Time(
                    timeInSeconds = nextSec,
                    isCompleted = counter >= durationInSec
                )
                postValue(nextValue)
                counter++
                if (counter > durationInSec) {
                    stop()
                }
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        javaTimer?.cancel()
        javaTimer = null
    }

    interface TimeSource {
        val startedTimestamp: Long

        fun saveStartedTimestamp(timestamp: Long)
    }

    data class Time(
        val isCompleted: Boolean,
        val timeInSeconds: Long
    )
}