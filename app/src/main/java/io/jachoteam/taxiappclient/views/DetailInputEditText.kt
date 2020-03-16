package io.jachoteam.taxiappclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import io.jachoteam.taxiappclient.R

class DetailInputEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        initView()
    }

    private fun initView() {
        setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                compoundDrawables[RIGHT_DRAWABLE]?.let {
                    if (motionEvent.rawX >= (right - it.bounds.width())) {
                        setText("")
                        return@setOnTouchListener true
                    }
                }
            }
            return@setOnTouchListener false
        }

        setOnFocusChangeListener { _, _ ->

        }
    }

    override fun setOnFocusChangeListener(listener: OnFocusChangeListener?) {
        super.setOnFocusChangeListener { view, focused ->
            val parent = parent.parent as? TextInputLayout
            if (focused) {
                parent?.error = null
            }
            val drawable = if (focused) R.drawable.ic_close else 0
            setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)

            listener?.onFocusChange(view, focused)
        }
    }

    companion object {
        private const val RIGHT_DRAWABLE = 2
    }
}