package io.jachoteam.taxiappclient.di.module

import dagger.Module
import dagger.Provides
import io.jachoteam.taxiappclient.di.util.BaseUrl
import io.jachoteam.taxiappclient.di.util.SMSVerification
import javax.inject.Singleton

@Module
class AppConstantsModule {

    @get:Provides
    @get:Singleton
    @get:BaseUrl
    val baseUrl: String = "YOUR_DOMAIN"

    @get:Provides
    @get:Singleton
    @get:SMSVerification
    val waitingDuration: Long = 360
}
