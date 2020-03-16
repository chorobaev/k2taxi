package io.jachoteam.taxiappclient.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels
import kotlinx.android.synthetic.main.state_card.view.*

class Card : RelativeLayout {

    private lateinit var content: LinearLayout
    private lateinit var activeViewContainer: FrameLayout
    private var onActivatedListener: (() -> Unit)? = null
    private var isActive = false

    constructor(context: Context) : super(context) {
        initView(-1, null, 0)
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        applyCustomAttrs(context, attr)
    }

    private fun applyCustomAttrs(context: Context, attr: AttributeSet) {
        val attrs = context.theme.obtainStyledAttributes(
                attr,
                R.styleable.Card,
                0, 0
        )
        val iconRes: Int
        val tariff: String?
        val price: Int
        try {
            iconRes = attrs.getInteger(R.styleable.Card_icon, -1)
            tariff = attrs.getString(R.styleable.Card_tariff)
            price = attrs.getInteger(R.styleable.Card_price, 0)
        } finally {
            attrs.recycle()
        }
        initView(iconRes, tariff, price)
    }

    private fun initView(iconRes: Int, tariff: String?, price: Int) {

        activeViewContainer = FrameLayout(context)
        activeViewContainer.setBackgroundResource(R.drawable.selected_tariff_bg)

        content = View.inflate(context, R.layout.state_card, null) as LinearLayout

        setIcon(iconRes)
        tariff?.let { setTariff(tariff) }
        setPrice(price)

        makePassive()
        setOnClickListener {
            setActive(!isActive)
            if (isActive) {
                onActivatedListener?.invoke()
            }
        }
    }

    fun setActive(isActive: Boolean) {
        if (isActive) {
            makeActive()
        } else {
            makePassive()
        }
    }

    fun setOnActivatedListener(listener: (() -> Unit)?) {
        onActivatedListener = listener
    }

    fun setTariff(tariff: String) {
        if (::content.isInitialized) {
            content.tv_tariff.text = tariff
        }
    }

    fun setIcon(iconResource: Int) {
        if (::content.isInitialized && iconResource != -1) {
            content.iv_icon.setImageResource(iconResource)
        }
    }

    fun setPrice(price: Int) {
        if (::content.isInitialized) {
            val prettyPrice = context.getString(R.string.card_price_int_som)
            content.tv_price.text = String.format(prettyPrice, price)
        }
    }

    private fun makeActive() {
        with(content) {
            iv_icon.alpha = 1f
            tv_tariff.alpha = 1f
            tv_price.typeface = Typeface.DEFAULT_BOLD
        }
        removeAllViews()
        activeViewContainer.removeAllViews()
        activeViewContainer.addView(content)
        addView(activeViewContainer)

        isActive = true
    }

    private fun makePassive() {
        with(content) {
            iv_icon.alpha = 0.5f
            tv_tariff.alpha = 0.5f
            tv_price.typeface = Typeface.DEFAULT
        }
        removeAllViews()
        activeViewContainer.removeAllViews()
        addView(content)

        isActive = false
    }
}