package io.sikorka.android.ui

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView


fun EditText.asString(): String = this.text.toString()

fun TextInputLayout.value(): String = this.editText?.asString() ?: ""

fun TextView.value(): String = this.text.toString()

fun TextInputLayout.setValue(text: String) {
  this.editText?.setText(text)
}

fun View.showShortSnack(@StringRes resId: Int) {
  Snackbar.make(this, resId, Snackbar.LENGTH_SHORT).show()
}

fun View.show() {
  visibility = View.VISIBLE
}

fun View.hide() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

val View.isVisible
  inline get() = visibility == View.VISIBLE


fun View.progressSnack(@StringRes resId: Int, duration: Int): Snackbar {
  val bar = Snackbar.make(this, resId, duration)
  val contentLay = bar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text).parent as ViewGroup
  val item = ProgressBar(context)
  contentLay.addView(item)
  return bar
}