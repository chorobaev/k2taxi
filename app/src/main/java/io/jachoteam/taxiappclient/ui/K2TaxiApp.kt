package io.jachoteam.taxiappclient.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.di.component.DaggerApplicationComponent
import io.jachoteam.taxiappclient.utilities.CHANNEL_ID

class K2TaxiApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.setSound(null, null)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder()
            .application(this)
            .build()
        component.inject(this)
        return component
    }

    companion object {
        private var instance: K2TaxiApp? = null

        fun getInstance(): K2TaxiApp = instance!!
    }
}