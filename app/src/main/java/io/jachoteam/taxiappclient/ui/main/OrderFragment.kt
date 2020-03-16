package io.jachoteam.taxiappclient.ui.main

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import io.jachoteam.taxiappclient.R
import io.jachoteam.taxiappclient.base.BaseFragment
import io.jachoteam.taxiappclient.models.local.Order
import io.jachoteam.taxiappclient.models.local.Tariff
import io.jachoteam.taxiappclient.utilities.makeShortToast
import io.jachoteam.taxiappclient.utilities.toVisibility
import io.jachoteam.taxiappclient.views.addCardsByTariffs
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private var shouldRequestRide = false

    override val layoutRes: Int get() = R.layout.fragment_order

    override fun onInitViewModel() {
        mainViewModel = getViewModel()
    }

    override fun onInitUI(firstInit: Boolean) {
        initAutofill()
        initTextInputs()
    }

    private fun initAutofill() {
        et_phone_number.setText(mainViewModel.phoneNumber)
    }

    private fun initTextInputs() {
        et_phone_number.addTextChangedListener {
            enableButtonIfPossible()
        }

        et_pickup.addTextChangedListener {
            enableButtonIfPossible()
        }
    }

    private fun enableButtonIfPossible() {
        btn_order.isEnabled =
            et_phone_number.text?.length ?: 0 >= 9 && et_pickup.text?.length ?: 0 >= 3
    }

    override fun onObserveViewModel() {
        mainViewModel.loading.observe(this, Observer { loading ->
            pb_loading.visibility = loading.toVisibility()
        })

        mainViewModel.timeUntilNextOrder.observe(this, Observer {
            if (it != null) {
                btn_order.setTime(it)
            }
        })

        mainViewModel.tariffs.observe(this, Observer { tariffs ->
            tariffs?.let {
                onTariffsReceived(it)
                if (shouldRequestRide) {
                    requestRide()
                    shouldRequestRide = false
                }
            }
        })

        mainViewModel.rideRequestSucceeded.observe(this, Observer {
            onRequestAccepted()
        })
    }

    private fun onTariffsReceived(tariffs: List<Tariff>) {
        card_group.addCardsByTariffs(tariffs)
    }

    private fun requestRide() {
        if (mainViewModel.loading.value == true) {
            baseActivity.makeShortToast(R.string.main_msg_patience)
            return
        }

        val tariffId = mainViewModel.getTariffId(card_group.selectedCardPosition)
        val phoneNumber = et_phone_number.text.toString()
        val location = mainViewModel.location.value!!
        val order = Order(
            tariffId = tariffId,
            phone = "0$phoneNumber",
            address = et_pickup.text.toString(),
            latitude = location.latitude,
            longitude = location.longitude
        )

        if (tariffId == -1) {
            shouldRequestRide = true
            mainViewModel.requestTariffs()
            return
        }

        mainViewModel.requestRide(order)
    }

    private fun onRequestAccepted() {
        mainViewModel.showNotification(getString(R.string.main_msg_ride_accepted))
        et_pickup.setText("")
    }

    override fun onSetOnClickListeners() {
        btn_order.setOnClickListener {
            requestRide()
        }

        btn_order.setOnDisabledListener { disabled ->
            if (disabled) {
                mainViewModel.showNotification(
                    getString(R.string.main_warning_cannot_order_frequently)
                )
            }
        }
    }
}