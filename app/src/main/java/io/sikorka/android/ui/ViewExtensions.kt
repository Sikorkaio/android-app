package io.sikorka.android.ui

import android.content.Context
import android.text.SpannedString
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.sikorka.android.R

fun EditText.asString(): String = this.text.toString()

fun TextInputLayout.value(): String = this.editText?.asString() ?: ""

fun TextInputLayout.clearError() {
  this.error = null
}

fun TextView.value(): String = this.text.toString()

fun TextInputLayout.setValue(text: String) {
  this.editText?.setText(text)
}

fun View.showShortSnack(@StringRes resId: Int) {
  Snackbar.make(this, resId, Snackbar.LENGTH_SHORT).show()
}

fun View.progressSnack(@StringRes resId: Int, duration: Int): Snackbar {
  val bar = Snackbar.make(this, resId, duration)
  val id = com.google.android.material.R.id.snackbar_text
  val contentLay = bar.view.findViewById<TextView>(id).parent as ViewGroup
  val item = ProgressBar(context)
  contentLay.addView(item)
  return bar
}

fun Context.coloredSpan(@StringRes resId: Int): SpannedString {
  return buildSpannedString {
    color(ContextCompat.getColor(this@coloredSpan, R.color.colorAccent)) {
      append(getString(resId))
    }
  }
}

fun Fragment.coloredSpan(@StringRes resId: Int): SpannedString {
  return requireContext().coloredSpan(resId)
}