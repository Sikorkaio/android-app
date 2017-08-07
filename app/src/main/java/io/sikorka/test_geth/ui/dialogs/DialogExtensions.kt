package io.sikorka.test_geth.ui.dialogs

import android.content.Context
import android.support.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog

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