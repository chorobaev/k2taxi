package io.jachoteam.taxiappclient.utilities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import io.jachoteam.taxiappclient.R

val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

const val LOCATION_PERMISSION_CODE = 111

val Context.locationPermissionsGranted: Boolean
    get() = isPermissionsGranted(LOCATION_PERMISSIONS)

fun AppCompatActivity.requestLocationPermissions() {
    requestPermission(LOCATION_PERMISSIONS, true)
}

fun Context.isPermissionsGranted(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        permission
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }

    return true
}

fun AppCompatActivity.requestPermission(
        permissions: Array<String>,
        finishActivity: Boolean
) {
    var shouldShowRationaleDialog = false

    for (permission in permissions) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            shouldShowRationaleDialog = true
        }
    }

    if (shouldShowRationaleDialog) {
        RationaleDialog.newInstance(LOCATION_PERMISSION_CODE, finishActivity)
                .show(supportFragmentManager, "dialog")
    } else {
        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE)
    }
}

class RationaleDialog : DialogFragment() {

    private var mFinishActivity = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments!!
        val requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE)
        mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY)
        val msgRes: Int
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> msgRes =
                    R.string.permission_rationale_location
            else -> throw IllegalArgumentException("No such permission")
        }

        return AlertDialog.Builder(requireActivity())
                .setMessage(msgRes)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            requestCode
                    )
                    mFinishActivity = false
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mFinishActivity) {
            Toast.makeText(activity, R.string.permission_required_toast, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    companion object {

        private const val ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode"

        private const val ARGUMENT_FINISH_ACTIVITY = "finish"

        fun newInstance(requestCode: Int, finishActivity: Boolean): RationaleDialog {
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode)
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity)
            val dialog = RationaleDialog()
            dialog.arguments = arguments
            return dialog
        }
    }
}

class PermissionDeniedDialog private constructor() : DialogFragment() {

    private var mFinishActivity = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mFinishActivity = arguments!!.getBoolean(ARGUMENT_FINISH_ACTIVITY)

        return AlertDialog.Builder(requireActivity())
                .setMessage(R.string.location_permission_denied)
                .setPositiveButton(android.R.string.ok, null)
                .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mFinishActivity) {
            Toast.makeText(activity, R.string.permission_required_toast, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    companion object {
        private const val ARGUMENT_FINISH_ACTIVITY = "finish"

        fun newInstance(finishActivity: Boolean): PermissionDeniedDialog {
            val arguments = Bundle()
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity)

            val dialog = PermissionDeniedDialog()
            dialog.arguments = arguments
            return dialog
        }
    }
}