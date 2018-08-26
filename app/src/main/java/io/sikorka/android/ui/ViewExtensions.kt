package io.sikorka.android.ui

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.text.SpannedString
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import io.sikorka.android.R

fun EditText.asString(): String = this.text.toString()

fun com.google.android.material.textfield.TextInputLayout.value(): String = this.editText?.asString() ?: ""

fun com.google.android.material.textfield.TextInputLayout.clearError() {
  this.error = null
}

fun TextView.value(): String = this.text.toString()

fun com.google.android.material.textfield.TextInputLayout.setValue(text: String) {
  this.editText?.setText(text)
}

fun View.showShortSnack(@StringRes resId: Int) {
  com.google.android.material.snackbar.Snackbar.make(this, resId, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
}

fun View.progressSnack(@StringRes resId: Int, duration: Int): com.google.android.material.snackbar.Snackbar {
  val bar = com.google.android.material.snackbar.Snackbar.make(this, resId, duration)
  val contentLay =
    bar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).parent as ViewGroup
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

fun androidx.fragment.app.Fragment.coloredSpan(@StringRes resId: Int): SpannedString {
  return requireContext().coloredSpan(resId)
}