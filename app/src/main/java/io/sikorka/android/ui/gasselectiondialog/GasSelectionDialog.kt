package io.sikorka.android.ui.gasselectiondialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputLayout
import io.sikorka.android.R
import io.sikorka.android.core.EtherUnits
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.findUnit
import io.sikorka.android.core.valueToUnit
import io.sikorka.android.core.valueToWei
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.coloredSpan

class GasSelectionDialog : DialogFragment() {

  private var dialog: AlertDialog? = null

  private lateinit var fm: FragmentManager
  private lateinit var gas: ContractGas
  private lateinit var onGasSelected: (gas: ContractGas) -> Unit

  private lateinit var gasPriceInput: TextInputLayout
  private lateinit var gasPriceUnit: Spinner
  private lateinit var gasLimitInput: TextInputLayout
  private lateinit var gasLimitUnit: Spinner

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__gas_selection, null, false)
    gasPriceInput = view.findViewById(R.id.gas_selection__gas_price)
    gasPriceUnit = view.findViewById(R.id.gas_selection__gas_price_currency)
    gasLimitInput = view.findViewById(R.id.gas_selection__gas_limit)
    gasLimitUnit = view.findViewById(R.id.gas_selection__gas_limit_currency)

    val builder = AlertDialog.Builder(context)
      .setTitle(coloredSpan(R.string.gas_selection__dialog_title))
      .setView(view)
      .setPositiveButton(coloredSpan(R.string.gas_selection__positive_text)) { dialog, _ ->
        onGasSelected(gas())
        dialog.dismiss()
      }
      .setNegativeButton(coloredSpan(android.R.string.cancel)) { dialog, _ ->
        dialog.dismiss()
      }

    val arrayAdapter =
      ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, EtherUnits.units)
    gasLimitUnit.adapter = arrayAdapter
    gasPriceUnit.adapter = arrayAdapter

    gasLimit = gas.limit
    gasPrice = gas.price

    val dialog = builder.create()
    this.dialog = dialog
    return dialog
  }

  fun show() {
    show(fm, TAG)
  }

  override fun dismiss() {
    dialog?.dismiss()
  }

  private fun gas(): ContractGas {
    return ContractGas(gasPrice, gasLimit)
  }

  private var gasPrice: Long
    get() {
      val editText = gasPriceInput.editText ?: fail("edit text was null")
      val unit = gasPriceUnit.selectedItem as? String ?: fail("invalid unit")
      val price = editText.text.toString()
      return valueToWei(price.toLong(), unit)
    }
    set(value) {
      val editText = gasPriceInput.editText ?: fail("edit text was null")
      val gasUnit = findUnit(value)
      val gasPrice = valueToUnit(value, gasUnit)
      editText.setText(gasPrice.toString())
      gasPriceUnit.setSelection(EtherUnits.units.indexOf(gasUnit))
    }

  private var gasLimit: Long
    get() {
      val editText = gasLimitInput.editText ?: fail("editText was null for gasLimit")
      val unit = gasLimitUnit.selectedItem as? String ?: fail("invalid unit")
      val limit = editText.text.toString()
      return valueToWei(limit.toLong(), unit)
    }
    set(value) {
      val editText = gasLimitInput.editText ?: fail("editText was null")
      val gasLimUnit = findUnit(value)
      val gasLimit = valueToUnit(value, gasLimUnit)
      editText.setText(gasLimit.toString())
      gasLimitUnit.setSelection(EtherUnits.units.indexOf(gasLimUnit))
    }

  companion object {
    const val TAG = "io.sikorka.android.ui.gasselectiondialog"

    fun create(
      fm: FragmentManager,
      gas: ContractGas,
      onGasSelected: (gas: ContractGas) -> Unit
    ): GasSelectionDialog {
      val dialog = GasSelectionDialog()
      dialog.fm = fm
      dialog.gas = gas
      dialog.onGasSelected = onGasSelected
      return dialog
    }
  }
}