package io.jachoteam.taxiappclient.di.module

import dagger.Module
import dagger.Provides
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.di.util.AppVersionDialog
import io.jachoteam.taxiappclient.di.util.LogoutConfirmDialog
import io.jachoteam.taxiappclient.utilities.ConfirmDialog
import io.jachoteam.taxiappclient.utilities.GpsRequiredDialog
import io.jachoteam.taxiappclient.utilities.PermissionDeniedDialog

@Module
class DialogModule {

    @Provides
    fun provideGpsRequiredDialog(): GpsRequiredDialog =
        GpsRequiredDialog()

    @Provides
    fun provideLocationPermissionDeniedDialog(): PermissionDeniedDialog =
        PermissionDeniedDialog.newInstance(true)

    @Provides
    @AppVersionDialog
    fun provideAppVersionDialog(): ConfirmDialog {
        return ConfirmDialog(R.string.dialog_version_new_version, R.string.dialog_version_message)
    }

    @Provides
    @LogoutConfirmDialog
    fun provideLogoutConfirmDialog(): ConfirmDialog {
        return ConfirmDialog(R.string.dialog_logout_title, R.string.dialog_logout_message)
    }
}