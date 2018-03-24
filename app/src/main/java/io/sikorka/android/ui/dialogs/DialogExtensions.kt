package io.sikorka.android.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.support.annotation.StringRes
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.LayoutInflater
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.ui.coloredSpan
import io.sikorka.android.ui.dialogs.fileselection.FileSelectionDialog
import io.sikorka.android.ui.value

fun Context.showConfirmation(
  @StringRes title: Int,
  @StringRes content: Int,
  action: () -> Unit
) {
  AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setMessage(content)
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ ->
      action()
      dialog.dismiss()
    })
    .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
    .show()
}

fun Context.showConfirmation(
  @StringRes title: Int,
  @StringRes content: Int,
  contentMessage: String,
  action: () -> Unit
) {
  AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setMessage(getString(content, contentMessage))
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ ->
      action()
      dialog.dismiss()
    })
    .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
    .show()
}

fun Context.showInfo(
  @StringRes title: Int,
  @StringRes content: Int,
  contentMessage: String,
  action: () -> Unit
) {
  AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setMessage(getString(content, contentMessage))
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ ->
      action()
      dialog.dismiss()
    }).show()
}

fun Context.showInfo(
  @StringRes title: Int,
  @StringRes content: Int
) {
  AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setMessage(content)
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ ->
      dialog.dismiss()
    }).show()
}

fun Context.showInfo(
  @StringRes title: Int,
  @StringRes content: Int,
  action: () -> Unit
) {
  AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setMessage(content)
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ ->
      action()
      dialog.dismiss()
    }).show()
}

fun Context.verifyPassphraseDialog(onInput: (input: String) -> Unit) {
  val inputLayout = TextInputLayout(this)
  val editText = TextInputEditText(this)
  editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
  inputLayout.hint = getString(R.string.dialog__input_hint)
  inputLayout.addView(editText)

  val passphraseDialog = AlertDialog.Builder(this)
    .setTitle(coloredSpan(R.string.dialog__passphrase_title))
    .setMessage(R.string.dialog__passphrase_content)
    .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
    .create()

  passphraseDialog.setOnShowListener {
    val button = passphraseDialog.getButton(AlertDialog.BUTTON_POSITIVE)
    button.setOnClickListener {
      onInput(editText.value())
    }
  }

  passphraseDialog.show()
}

@SuppressLint("InflateParams")
fun Context.progress(
  @StringRes title: Int,
  @StringRes content: Int
): AlertDialog {
  val inflater = LayoutInflater.from(this)
  val view = inflater.inflate(R.layout.dialog__progress, null, false)
  val textView = view.findViewById<TextView>(R.id.progress_dialog__text)
  textView.setText(content)
  return AlertDialog.Builder(this)
    .setTitle(coloredSpan(title))
    .setView(R.layout.dialog__progress)
    .create()
}

fun Context.useDetector(
  callback: (useDetector: Boolean) -> Unit
): AlertDialog = AlertDialog.Builder(this)
  .setTitle(coloredSpan(R.string.use_detectors__dialog_title))
  .setMessage(R.string.use_detectors__dialog_content)
  .setPositiveButton(coloredSpan(R.string.use_detectors__positive), { dialog, _ ->
    callback(true)
    dialog.dismiss()
  })
  .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
  .setNeutralButton(coloredSpan(R.string.use_detectors__neutral), { dialog, _ ->
    callback(false)
    dialog.dismiss()
  })
  .create()

@SuppressLint("InflateParams")
fun Context.balancePrecisionDialog(
  currentPrecision: Int,
  callback: (digits: Int) -> Unit
): AlertDialog {
  val inflater = LayoutInflater.from(this@balancePrecisionDialog)
  val builder = AlertDialog.Builder(this)
  val view = inflater.inflate(R.layout.dialog__balance_precision, null)
  val inputLayout: TextInputLayout = view.findViewById(R.id.balance_precision__digits_input)
  val editText: TextInputEditText = view.findViewById(R.id.balance_precision__digits_text)
  builder.setView(view)
    .setTitle(R.string.balance_precision__title)
    .setCancelable(false)
  val dialog = builder.setPositiveButton(android.R.string.ok, { _, _ -> })
    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
      with(dialog) {
        cancel()
        dismiss()
      }
    }.create()

  editText.setText(currentPrecision.toString())

  dialog.setOnShowListener {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
      val value = editText.value()
      val number = value.toIntOrNull()

      inputLayout.error = null

      if (number == null) {
        inputLayout.error = getString(R.string.balance_precision__invalid_input)
        return@setOnClickListener
      }

      val rangeStart = 0
      val rangeEnd = 15

      if (number < rangeStart || number > rangeEnd) {
        inputLayout.error = getString(
          R.string.balance_precision__invalid_range,
          rangeStart,
          rangeEnd
        )
        return@setOnClickListener
      }

      callback(number)
      dialog.dismiss()
    }
  }

  return dialog
}

@SuppressLint("InflateParams")
fun Context.loading(@StringRes setTitleResId: Int, @StringRes contentResId: Int): AlertDialog {
  val inflater = LayoutInflater.from(this)
  val view = inflater.inflate(R.layout.dialog__progress, null)
  view.findViewById<TextView>(R.id.progress_dialog__text).setText(contentResId)
  return AlertDialog.Builder(this)
    .setTitle(coloredSpan(setTitleResId))
    .setView(view)
    .setCancelable(false)
    .create()
}

fun Context.createDialog(
  @StringRes setTitleResId: Int,
  message: CharSequence,
  onDismiss: () -> Unit
): AlertDialog {
  return AlertDialog.Builder(this)
    .setTitle(coloredSpan(setTitleResId))
    .setMessage(message)
    .setPositiveButton(coloredSpan(android.R.string.ok), { dialog, _ -> dialog.dismiss() })
    .setOnDismissListener { onDismiss() }
    .create()
}

fun AppCompatActivity.fileSelectionDialog(showFiles: Boolean = false): FileSelectionDialog {
  val directory = Environment.getExternalStorageDirectory()
  return FileSelectionDialog.create(supportFragmentManager, directory, showFiles)
}