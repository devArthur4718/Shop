package com.stetter.escambo.extension.watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


class MoneyTextWatcher : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText?>?
    private val locale: Locale?

    constructor(editText: EditText?, locale: Locale?) {
        editTextWeakReference = WeakReference<EditText?>(editText)
        this.locale = if (locale != null) locale else Locale.getDefault()
    }

    constructor(editText: EditText?) {
        editTextWeakReference = WeakReference<EditText?>(editText)
        locale = Locale.getDefault()
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable?) {
        val editText: EditText = editTextWeakReference?.get() ?: return
        editText.removeTextChangedListener(this)
        val parsed: BigDecimal? = parseToBigDecimal(editable.toString(), locale)
        val formatted: String = NumberFormat.getCurrencyInstance(locale).format(parsed)
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(value: String?, locale: Locale?): BigDecimal? {
        val replaceable = java.lang.String.format(
            "[%s,.\\s]",
            NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol()
        )
        val cleanString = value!!.replace(replaceable.toRegex(), "")
        return BigDecimal(cleanString).setScale(
            2, BigDecimal.ROUND_FLOOR
        ).divide(
            BigDecimal(100), BigDecimal.ROUND_FLOOR
        )
    }
}