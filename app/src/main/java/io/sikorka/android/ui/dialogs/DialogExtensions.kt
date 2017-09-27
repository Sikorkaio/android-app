package io.sikorka.android.ui.dialogs

import android.content.Context
import android.os.Environment
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.folderselector.FileChooserDialog
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import io.sikorka.android.R

fun Context.showConfirmation(
    @StringRes title: Int,
    @StringRes content: Int,
    action: () -> Unit
) {
  MaterialDialog.Builder(this)
      .title(title)
      .content(content)
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
      .content(getString(content, contentMessage))
      .positiveText(android.R.string.ok)
      .onPositive { dialog, _ ->
        action.invoke()
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
      .title(R.string.dialog__passphrase_title)
      .content(R.string.dialog__passphrase_content)
      .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
      .input(
          R.string.dialog__input_hint,
          R.string.dialog_passphrase__input_prefill,
          { dialog, input ->
            onInput.invoke(input.toString())
            dialog.dismiss()
          }
      ).show()
}