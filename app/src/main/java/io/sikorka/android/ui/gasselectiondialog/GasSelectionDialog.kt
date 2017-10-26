package io.sikorka.android.ui.gasselectiondialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.EtherUnits
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.findUnit
import io.sikorka.android.node.valueToUnit
import io.sikorka.android.node.valueToWei
import toothpick.Toothpick

class GasSelectionDialog : DialogFragment() {

  private var dialog: MaterialDialog? = null

  private lateinit var fm: FragmentManager
  private lateinit var gas: ContractGas
  private lateinit var onGasSelected: (gas: ContractGas) -> Unit

  @BindView(R.id.gas_selection__gas_price)
  lateinit var gasPriceInput: TextInputLayout

  @BindView(R.id.gas_selection__gas_price_currency)
  lateinit var gasPriceUnit: Spinner

  @BindView(R.id.gas_selection__gas_limit)
  lateinit var gasLimitInput: TextInputLayout

  @BindView(R.id.gas_selection__gas_limit_currency)
  lateinit var gasLimitUnit: Spinner

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val scope = Toothpick.openScopes(context.applicationContext, this)
    Toothpick.inject(this, scope)

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__gas_selection, null, false)
    ButterKnife.bind(this, view)

    val builder = MaterialDialog.Builder(context)
        .title(R.string.gas_selection__dialog_title)
        .titleColorRes(R.color.colorAccent)
        .customView(view, false)
        .positiveText(R.string.gas_selection__positive_text)
        .negativeText(android.R.string.cancel)
        .onPositive { dialog, _ ->
          onGasSelected(gas())
          dialog.dismiss()
        }
        .onNegative { dialog, _ -> dialog.dismiss() }

    val arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, EtherUnits.units)
    gasLimitUnit.adapter = arrayAdapter
    gasPriceUnit.adapter = arrayAdapter

    gasLimit = gas.limit
    gasPrice = gas.price

    val dialog = builder.build()
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

    fun create(fm: FragmentManager, gas: ContractGas, onGasSelected: (gas: ContractGas) -> Unit): GasSelectionDialog {
      val dialog = GasSelectionDialog()
      dialog.fm = fm
      dialog.gas = gas
      dialog.onGasSelected = onGasSelected
      return dialog
    }
  }
}