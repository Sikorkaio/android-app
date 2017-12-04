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
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.folderselector.FileChooserDialog
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import io.sikorka.android.R
import io.sikorka.android.ui.value

fun Context.showConfirmation(
    @StringRes title: Int,
    @StringRes content: Int,
    action: () -> Unit
) {
  MaterialDialog.Builder(this)
      .title(title)
      .titleColorRes(R.color.colorAccent)
      .content(content)
      .positiveText(android.R.string.ok)
      .negativeText(android.R.string.cancel)
      .onPositive { dialog, _ ->
        action.invoke()
        dialog.dismiss()
      }.show()
}

fun Context.showConfirmation(
    @StringRes title: Int,
    @StringRes content: Int,
    contentMessage: String,
    action: () -> Unit
) {
  MaterialDialog.Builder(this)
      .title(title)
      .titleColorRes(R.color.colorAccent)
      .content(getString(content, contentMessage))
      .positiveText(android.R.string.ok)
      .negativeText(android.R.string.cancel)
      .onPositive { dialog, _ ->
        action.invoke()
        dialog.dismiss()
      }.show()
}

fun Context.showInfo(
    @StringRes title: Int,
    @StringRes content: Int,
    contentMessage: String,
    action: () -> Unit
) {
  MaterialDialog.Builder(this)
      .title(title)
      .titleColorRes(R.color.colorAccent)
      .content(getString(content, contentMessage))
      .positiveText(android.R.string.ok)
      .onPositive { dialog, _ ->
        action.invoke()
        dialog.dismiss()
      }.show()
}

fun Context.showInfo(
    @StringRes title: Int,
    @StringRes content: Int
) {
  MaterialDialog.Builder(this)
      .title(title)
      .titleColorRes(R.color.colorAccent)
      .content(content)
      .positiveText(android.R.string.ok)
      .onPositive { dialog, _ ->
        dialog.dismiss()
      }.show()
}

fun Context.showInfo(
    @StringRes title: Int,
    @StringRes content: Int,
    action: () -> Unit
) {
  MaterialDialog.Builder(this)
      .title(title)
      .titleColorRes(R.color.colorAccent)
      .content(content)
      .positiveText(android.R.string.ok)
      .onPositive { dialog, _ ->
        action()
        dialog.dismiss()
      }.show()
}


fun <ActivityType> ActivityType.selectDirectory() where ActivityType : AppCompatActivity,
                                                        ActivityType : FolderChooserDialog.FolderCallback {
  FolderChooserDialog.Builder(this)
      .chooseButton(R.string.md_choose_label)
      .initialPath(Environment.getExternalStorageDirectory().absolutePath)
      .tag("optional-identifier")
      .goUpLabel("Up")
      .show(this)
}


fun <ActivityType> ActivityType.selectFile() where ActivityType : AppCompatActivity,
                                                   ActivityType : FileChooserDialog.FileCallback {
  val dialog = FileChooserDialog.Builder(this)
      .tag("file-selection")
      .mimeType("*/*")
      .initialPath(Environment.getExternalStorageDirectory().absolutePath)
      .build()

  dialog.show(this)
}

fun Context.verifyPassphraseDialog(onInput: (input: String) -> Unit) {
  MaterialDialog.Builder(this)
      .titleColorRes(R.color.colorAccent)
      .title(R.string.dialog__passphrase_title)
      .content(R.string.dialog__passphrase_content)
      .negativeText(android.R.string.cancel)
      .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
      .onNegative { dialog, _ -> dialog.dismiss() }
      .input(
          R.string.dialog__input_hint,
          R.string.dialog_passphrase__input_prefill,
          { dialog, input ->
            onInput.invoke(input.toString())
            dialog.dismiss()
          }
      ).show()
}

fun Context.progress(
    @StringRes title: Int,
    @StringRes content: Int
): MaterialDialog = MaterialDialog.Builder(this)
    .title(title)
    .titleColorRes(R.color.colorAccent)
    .progress(true, 100)
    .content(content)
    .build()

fun Context.useDetector(
    callback: (useDetector: Boolean) -> Unit
): MaterialDialog = MaterialDialog.Builder(this)
    .buttonsGravity(GravityEnum.CENTER)
    .btnStackedGravity(GravityEnum.END)
    .title(R.string.use_detectors__dialog_title)
    .titleColorRes(R.color.colorAccent)
    .content(R.string.use_detectors__dialog_content)
    .positiveText(R.string.use_detectors__positive)
    .neutralText(R.string.use_detectors__neutral)
    .negativeText(android.R.string.cancel)
    .onPositive { dialog, _ ->
      callback(true)
      dialog.dismiss()
    }
    .onNeutral { dialog, _ ->
      callback(false)
      dialog.dismiss()
    }
    .onNegative { dialog, _ -> dialog.dismiss() }
    .build()


@SuppressLint("InflateParams")
fun Context.balancePrecisionDialog(callback: (digits: Int) -> Unit): AlertDialog {
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

  dialog.setOnShowListener {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
      val value = editText.value()
      val number = value.toIntOrNull()

      inputLayout.error = null

      if (number == null) {
        inputLayout.error = getString(R.string.balance_precision__invalid_input)
        return@setOnClickListener
      }

      if (number < 0 || number > 10) {
        inputLayout.error = getString(R.string.balance_precision__invalid_range)
        return@setOnClickListener
      }

      callback(number)
      dialog.dismiss()
    }
  }


  return dialog
}