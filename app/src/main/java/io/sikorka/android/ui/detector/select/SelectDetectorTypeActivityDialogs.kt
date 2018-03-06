package io.sikorka.android.ui.detector.select

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.widget.Toast
import io.sikorka.android.R
import io.sikorka.android.ui.coloredSpan
import io.sikorka.android.ui.value
import org.ethereum.geth.Address
import org.ethereum.geth.Geth

fun SelectDetectorTypeActivity.addressSpecificationDialog(
  onAddress: (address: Address) -> Unit
): AlertDialog {
  val inputLayout = TextInputLayout(this)
  val editText = TextInputEditText(this)
  editText.inputType = InputType.TYPE_CLASS_TEXT
  inputLayout.hint = getString(R.string.select_detector__manual_dialog_hint)
  inputLayout.addView(editText)

  val dialog: AlertDialog = AlertDialog.Builder(this)
    .setTitle(coloredSpan(R.string.select_detector__manual_dialog_title))
    .setMessage(R.string.select_detector__manual_dialog_content)
    .setView(inputLayout)
    .setCancelable(false)
    .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
    .setPositiveButton(coloredSpan(android.R.string.ok), { _, _ -> })
    .create()

  dialog.setOnShowListener {
    val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    button.setOnClickListener {
      val input = editText.value()
      val address = if (input.startsWith("0x")) {
        input.replace("0x", "")
      } else {
        input
      }

      val addr = try {
        Geth.newAddressFromHex(address)
      } catch (ex: Exception) {
        null
      }

      if (addr == null) {
        Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show()
      } else {
        onAddress(addr)
      }
    }
  }

  return dialog
}