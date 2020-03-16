package io.jachoteam.taxiappclient.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import io.jachoteam.taxiappclient.di.module.ApplicationModule
import io.jachoteam.taxiappclient.di.module.ContextModule
import io.jachoteam.taxiappclient.di.module.RepositoryModule
import io.jachoteam.taxiappclient.ui.K2TaxiApp
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    RepositoryModule::class
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: K2TaxiApp)
}