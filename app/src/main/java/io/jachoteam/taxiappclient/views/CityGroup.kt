package io.jachoteam.taxiappclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import io.jachoteam.taxiappclient.models.network.CityInfo
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels

class CityGroup(context: Context, attr: AttributeSet? = null) : RadioGroup(context, attr) {

    private var buttons = ArrayList<RadioButton>()
    private var cities = ArrayList<CityInfo>()
    private var onCityChangedListener: ((CityInfo) -> Unit)? = null

    init {
        orientation = LinearLayout.HORIZONTAL
    }

    fun addCities(list: List<CityInfo>) {
        cities.addAll(list)
        addButtons(list)
    }

    fun addCity(city: CityInfo) {
        addButton(city)
    }

    fun clear() {
        removeAllViews()
        cities = arrayListOf()
        buttons = arrayListOf()
    }

    fun getSelectedCity(): CityInfo = cities[checkedRadioButtonId - 100]

    fun setOnCityChangedListener(listener: ((CityInfo) -> Unit)?) {
        onCityChangedListener = listener
    }

    private fun addButtons(list: List<CityInfo>) {
        list.forEach { addButton(it) }
    }

    private fun addButton(city: CityInfo) {
        createRadioButton(city).also {
            if (buttons.isEmpty()) it.isChecked = true
            it.id = buttons.size + 100
            buttons.add(it)
            addView(it)
        }.setOnClickListener {
            onCityChangedListener?.invoke(getSelectedCity())
        }
    }

    private fun createRadioButton(city: CityInfo): RadioButton {
        return RadioButton(context).apply {
            text = city.ru
            textSize = 16F
            val layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            val margin = 16.inDensity.toPixels(resources.displayMetrics).toInt()
            layoutParams.setMargins(0, 0, margin, 0)
            this.layoutParams = layoutParams
        }
    }
}