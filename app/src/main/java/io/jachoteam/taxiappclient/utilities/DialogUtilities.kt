package io.jachoteam.taxiappclient.utilities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.jachoteam.taxiappclient.R
import kotlinx.android.synthetic.main.dialog_verify_phone_number.*

class GpsRequiredDialog : DialogFragment() {
    private var onPositiveClickListener: DialogInterface.OnClickListener? = null
    private var onNegativeClickListener: DialogInterface.OnClickListener? = null
    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onKeyListener: DialogInterface.OnKeyListener? = null

    fun setPositiveClickedRunnable(runnable: Runnable) {
        this.onPositiveClickListener =
            DialogInterface.OnClickListener { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
    }

    fun setNegativeClickedRunnable(runnable: Runnable) {
        this.onNegativeClickListener =
            DialogInterface.OnClickListener { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
        this.onCancelListener =
            DialogInterface.OnCancelListener {
                dismiss()
                runnable.run()
            }
        this.onKeyListener = DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
                runnable.run()
            }
            true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.splash_dialog_gps_needed_msg)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, onPositiveClickListener)
            .setNegativeButton(android.R.string.cancel, onNegativeClickListener)
            .setOnCancelListener(onCancelListener)
            .setOnKeyListener(onKeyListener)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}

class PhoneNumberVerificationDialog(context: Context) : Dialog(context, R.style.DialogStyle) {

    init {
        setContentView(R.layout.dialog_verify_phone_number)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    var phoneNumber: String
        set(value) {
            tv_phone_number.text = value
        }
        get() = tv_phone_number.text.toString()

    fun setOnEditClickedListener(block: (() -> Unit)?) {
        tv_edit.setOnClickListener {
            hide()
            block?.invoke()
        }
    }

    fun setOnOKClickedListener(block: (() -> Unit)?) {
        tv_ok.setOnClickListener {
            dismiss()
            block?.invoke()
        }
    }

    override fun show() {
        super.show()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun show(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        this.show()
    }
}

class ProgressDialog(context: Context) : Dialog(context, R.style.DialogStyle) {

    init {
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}

class ConfirmDialog(
    @StringRes private val titleRes: Int,
    @StringRes private val messageRes: Int
) : DialogFragment() {
    private var onPositiveClickListener: DialogInterface.OnClickListener? = null
    private var onNegativeClickListener: DialogInterface.OnClickListener? = null
    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var onKeyListener: DialogInterface.OnKeyListener? = null

    fun setPositiveClickedRunnable(runnable: Runnable) {
        this.onPositiveClickListener =
            DialogInterface.OnClickListener { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
    }

    fun setNegativeClickedRunnable(runnable: Runnable) {
        this.onNegativeClickListener =
            DialogInterface.OnClickListener { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
        this.onCancelListener =
            DialogInterface.OnCancelListener {
                dismiss()
                runnable.run()
            }
        this.onKeyListener = DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
                runnable.run()
            }
            true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val textView = TextView(context)
        textView.run {
            setText(titleRes)
            val horizontalPadding = 24.inDensity.toPixels(resources.displayMetrics).toInt()
            val verticalPadding = 16.inDensity.toPixels(resources.displayMetrics).toInt()
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, 0)
            textSize = 18f
            setTypeface(typeface, Typeface.BOLD)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setCustomTitle(textView)
            .setMessage(messageRes)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, onPositiveClickListener)
            .setNegativeButton(android.R.string.cancel, onNegativeClickListener)
            .setOnCancelListener(onCancelListener)
            .setOnKeyListener(onKeyListener)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}