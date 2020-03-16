package io.jachoteam.taxiappclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.jachoteam.taxiappclient.R
import kotlinx.android.synthetic.main.view_code_resending.view.*

class CodeResendingView(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private var onResendListener: (() -> Unit)? = null

    var time: Long = 0
        set(value) {
            if (value == field) return
            field = value
            context?.let {
                tv_timer.text = it.getString(
                    R.string.auth_msg_resend_verification_code_in_long_seconds,
                    value
                )
            }
        }

    var isCompleted: Boolean = false
        set(value) {
            if (value == field) return
            field = value
            context?.let {
                if (value) {
                    tv_timer.visibility = View.INVISIBLE
                    tv_resend.visibility = View.VISIBLE
                } else {
                    tv_timer.visibility = View.VISIBLE
                    tv_resend.visibility = View.INVISIBLE
                }
            }
        }

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_code_resending, this, true)

        tv_resend.setOnClickListener {
            onResendListener?.invoke()
        }
    }

    fun setOnResendListener(listener: (() -> Unit)?) {
        onResendListener = listener
    }
}