package io.jachoteam.taxiappclient.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels

class AppNotification : CardView {
    private lateinit var message: TextView
    private lateinit var mHandler: Handler
    private val showingAnim = AnimationUtils.loadAnimation(context, R.anim.notification_show_up)
    private val hidingAnim = AnimationUtils.loadAnimation(context, R.anim.notification_hide_down)

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        initView()
    }

    private fun initView() {
        mHandler = Handler()

        visibility = View.GONE
        cardElevation = 4.inDensity.toPixels(resources.displayMetrics)
        radius = 4.inDensity.toPixels(resources.displayMetrics)

        message = TextView(context).apply {
            textSize = 18f
        }
        16.inDensity.toPixels(resources.displayMetrics).toInt().also {
            message.setPadding(it, it, it, it)
        }

        message.compoundDrawablePadding = 12.inDensity.toPixels(resources.displayMetrics).toInt()

        addView(message)
    }

    fun showNotification(msg: String) {
        message.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_check_decagram, 0, 0, 0)
        show(msg)
    }

    fun showNotification(msgRes: Int) {
        showNotification(resources.getString(msgRes))
    }

    fun showErrorNotification(msg: String) {
        message.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_alert_circle_outline, 0, 0, 0)
        show(msg)
    }

    fun showErrorNotification(msgRes: Int) {
        showErrorNotification(resources.getString(msgRes))
    }

    private fun show(msg: String) {
        mHandler.removeCallbacksAndMessages(null)
        message.text = msg
        visibility = View.VISIBLE
        startAnimation(showingAnim)
        Log.d(TAG, msg)

        mHandler.postDelayed({
            visibility = View.GONE
            startAnimation(hidingAnim)
        }, SHORT)
    }

    companion object {
        private const val TAG = "MylogAppNotification"
        const val LONG = 5000L
        const val SHORT = 3000L
    }
}