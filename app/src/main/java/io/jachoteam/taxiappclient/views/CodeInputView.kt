package io.jachoteam.taxiappclient.views

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.KeyEvent
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import io.jachoteam.taxiappclient.utilities.inDensity
import io.jachoteam.taxiappclient.utilities.toPixels

class CodeInputView(context: Context, attr: AttributeSet? = null)
    : LinearLayout(context, attr) {

    private val cells = ArrayList<AppCompatEditText>()
    var length = 4
        private set

    private var onCodeCompetedListener: ((Boolean) -> Unit)? = null
    val code: String
        get() = buildString {
            cells.forEach {
                if (it.text.isNullOrEmpty()) append('-') else append(it.text.toString())
            }
        }

    fun setOnCodeChangedListener(block: ((completed: Boolean) -> Unit)? = null) {
        onCodeCompetedListener = block
    }

    fun clear() {
        cells.forEach { it.setText("") }
        cells[0].requestFocus()
    }

    init {
        orientation = HORIZONTAL
        gravity = CENTER
        initCells()
    }

    private fun initCells() {
        for (i in 0 until length) {
            addView(getCell(i))
        }
        cells[0].requestFocus()
    }

    private fun getCell(id: Int): AppCompatEditText {
        val editText = AppCompatEditText(context).apply {
            this.id = id
            textSize = 26F
            width = 24.inDensity.toPixels(context.resources.displayMetrics).toInt()
            filters = arrayOf(InputFilter.LengthFilter(1))
            isCursorVisible = false
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        editText.setOnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (editText.text.isNullOrEmpty() && id > 0) {
                    cells[id - 1].run {
                        requestFocus()
                        setText("")
                    }
                } else {
                    editText.setText("")
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        if (cells.isNotEmpty()) {
            cells[cells.size - 1].addTextChangedListener { editable ->
                if (editable != null && editable.isNotEmpty()) {
                    checkIfCodeWritingCompeted()
                    focusOnNextCell(id)
                }
            }
        }

        if (id == length - 1) {
            editText.addTextChangedListener { editable ->
                if (editable != null && editable.isNotEmpty()) {
                    checkIfCodeWritingCompeted()
                }
            }
        }

        cells.add(editText)

        return editText
    }

    private fun checkIfCodeWritingCompeted() {
        onCodeCompetedListener?.invoke(
            cells.all {
                !it.text.isNullOrEmpty()
            }
        )
    }

    private fun focusOnNextCell(currentId: Int) {
        var id = currentId
        while (id < length - 1 && !cells[id].text.isNullOrEmpty()) id++
        cells[id].requestFocus()
    }
}