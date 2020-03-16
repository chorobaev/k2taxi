package io.jachoteam.taxiappclient.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import io.jachoteam.taxiappclient.R

class RequestOrderButton(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    private var disabled = false
    private var disabledListener: ((disabled: Boolean) -> Unit)? = null

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            if (!disabled) {
                l?.onClick(it)
            }
            disabledListener?.invoke(disabled)
        }
    }

    fun setOnDisabledListener(listener: ((disabled: Boolean) -> Unit)?) {
        disabledListener = listener
    }

    fun setTime(sec: Long) {
        disabled = sec != 0L
        if (!disabled) {
            text = resources.getString(R.string.main_btn_text_order)
        } else {
            setNonZeroTime(sec)
        }
    }

    private fun setNonZeroTime(sec: Long) {
        val minutes: String
        (sec / 60 % 60).also {
            minutes = if (it < 10) "0$it" else it.toString()
        }
        val seconds: String
        (sec % 60).also {
            seconds = if (it < 10) "0$it" else it.toString()
        }
        resources.getString(R.string.main_btn_text_order_with_timer).also {
            text = String.format(it, minutes, seconds)
        }
    }
}