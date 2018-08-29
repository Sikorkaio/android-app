package io.sikorka.android.ui.settings.peermanager

import android.annotation.SuppressLint
import android.util.Patterns.WEB_URL
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import io.sikorka.android.R
import io.sikorka.android.ui.clearError
import io.sikorka.android.ui.value

@SuppressLint("InflateParams")
fun PeerManagerActivity.urlInputDialog(
  onInput: (url: String, merge: Boolean) -> Unit
): AlertDialog {

  val inflater = LayoutInflater.from(this)
  val view = inflater.inflate(R.layout.dialog__peer_url, null, false)
  val input = view.findViewById<TextInputLayout>(R.id.peer_manager__url_dialog_input)
  val mergeCheckBox: CheckBox = view.findViewById(R.id.peer_manager__peers_merge_check)

  val dialog = AlertDialog.Builder(this)
    .setTitle(R.string.peer_manager__url_dialog_title)
    .setView(view)
    .setPositiveButton(R.string.peer_manager__url_dialog_positive_button, null)
    .setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
      dialogInterface.dismiss()
    }
    .create()

  dialog.setOnShowListener { dialogInterface ->
    val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    button.setOnClickListener {
      input.clearError()

      val text = input.value()
      if (text.isBlank()) {
        input.error = getString(R.string.peer_manager__url_dialog_empty_url)
        return@setOnClickListener
      }

      if (!WEB_URL.matcher(text).matches()) {
        input.error = getString(R.string.peer_manager__url_dialog_invalid_url)
        return@setOnClickListener
      }

      onInput(text, mergeCheckBox.isChecked)
      dialogInterface.dismiss()
    }
  }
  return dialog
}