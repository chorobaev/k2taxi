package io.jachoteam.taxiappclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import androidx.core.view.setMargins
import io.jachoteam.taxiappclient.models.local.Tariff
import io.jachoteam.taxiappclient.models.local.tariffIcon
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels

class CardGroup : HorizontalScrollView {

    private val cards: MutableList<Card> = arrayListOf()
    private lateinit var content: LinearLayout
    private var selectedCard = 0

    val selectedCardPosition: Int
        get() = selectedCard

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        content = LinearLayout(context)
        content.orientation = HORIZONTAL
        val padding = 8.inDensity.toPixels(content.resources.displayMetrics).toInt()
        content.setPadding(padding, 0, padding, 0)
        addView(content)
        isSmoothScrollingEnabled = true
    }

    fun selectCard(cardPosition: Int) {
        resetCards()
        cards[cardPosition].setActive(true)
        selectedCard = cardPosition
    }

    fun addCards(cards: List<Card>) {
        cards.forEach { addCard(it) }
    }

    fun addCard(card: Card) {
        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(8.inDensity.toPixels(resources.displayMetrics).toInt())
        card.layoutParams = params
        content.addView(card)
        addCardToCardGroupList(card)
    }

    private fun addCardToCardGroupList(card: Card) {
        if (cards.size == 0) {
            card.setActive(true)
        }
        val currentCard = cards.size
        card.setOnActivatedListener {
            resetCards()
            selectedCard = currentCard
            card.setActive(true)
            card.isClickable = false

            smoothScrollTo((currentCard - 1) * (card.width + 8), 0)
        }
        cards.add(card)
    }

    private fun resetCards() {
        cards.forEach {
            it.setActive(false)
            it.isClickable = true
        }
    }

    fun removeAll() {
        content.removeAllViews()
        cards.clear()
        selectedCard = 0
    }
}

fun CardGroup.addCardsByTariffs(tariffs: List<Tariff>) {
    val lastSelectedCardPosition = selectedCardPosition
    removeAll()
    tariffs.forEach { tariff ->
        with(tariff) {
            Card(context).apply {
                setIcon(tariffIcon)
                setTariff(name)
                setPrice(base.toInt())
            }.also {
                addCard(it)
            }
        }
    }
    try {
        selectCard(lastSelectedCardPosition)
    } catch (ignored: IndexOutOfBoundsException) {
    }
}