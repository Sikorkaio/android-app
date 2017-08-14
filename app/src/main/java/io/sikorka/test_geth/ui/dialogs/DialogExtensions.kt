package io.sikorka.test_geth.ui.dialogs

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.folderselector.FileChooserDialog
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import io.sikorka.test_geth.R

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

fun <ActivityType> ActivityType.selectDirectory() where ActivityType : AppCompatActivity,
  ActivityType : FolderChooserDialog.FolderCallback {
  FolderChooserDialog.Builder(this)
      .chooseButton(R.string.md_choose_label)
      .tag("optional-identifier")
      .goUpLabel("Up")
      .show()
}


fun <ActivityType> ActivityType.selectFile() where ActivityType : AppCompatActivity,
  ActivityType : FileChooserDialog.FileCallback {
  FileChooserDialog.Builder(this)
      .show()

}