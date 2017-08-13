package io.sikorka.test_geth.ui.dialogs

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
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

fun <ActivityType> ActivityType.selectDirectory()
    where ActivityType : AppCompatActivity, ActivityType : FolderChooserDialog.FolderCallback {
  FolderChooserDialog.Builder(this)
      .chooseButton(R.string.md_choose_label)  // changes label of the choose button
      .tag("optional-identifier")
      .goUpLabel("Up") // custom go up label, default label is "..."
      .show()
}