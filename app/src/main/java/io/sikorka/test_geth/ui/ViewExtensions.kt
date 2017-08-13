package io.sikorka.test_geth.ui

import android.support.design.widget.TextInputLayout
import android.widget.EditText
import android.widget.TextView

fun EditText.asString(): String = this.text.toString()

fun TextInputLayout.value(): String = this.editText?.asString() ?: ""

fun TextView.value(): String = this.text.toString()