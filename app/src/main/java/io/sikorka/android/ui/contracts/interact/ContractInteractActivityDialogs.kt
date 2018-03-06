package io.sikorka.android.ui.contracts.interact

import android.support.v7.app.AlertDialog
import io.sikorka.android.R
import io.sikorka.android.ui.coloredSpan

fun ContractInteractActivity.methodSelectDialog(
  options: Array<String>,
  onSelect: (item: String) -> Unit
): AlertDialog {
  var selectedItem = 0
  return AlertDialog.Builder(this)
    .setTitle(coloredSpan(R.string.contract_interact__select_method))
    .setPositiveButton(coloredSpan(R.string.contract_interact__select_method_choose), { dialog, _ ->
      onSelect(options[selectedItem])
      dialog.dismiss()
    })
    .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
    .setItems(options, { _, which ->
      selectedItem = which
    }).create()
}