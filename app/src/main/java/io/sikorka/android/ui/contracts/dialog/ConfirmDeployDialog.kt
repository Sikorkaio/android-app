package io.sikorka.android.ui.contracts.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.coloredSpan

class ConfirmDeployDialog : DialogFragment() {

  private lateinit var gasPriceWei: TextView
  private lateinit var gasLimitWei: TextView
  private lateinit var passphraseInput: TextInputLayout

  private lateinit var dialog: AlertDialog

  private lateinit var fm: FragmentManager
  private lateinit var onDeployConfirm: OnDeployConfirm
  private lateinit var contractGas: ContractGas

  private val passphraseField: EditText
    get() = passphraseInput.editText ?: fail("passphraseInput edittext was null")

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__confirm_deploy, null, false)

    dialog = AlertDialog.Builder(context)
      .setTitle(coloredSpan(R.string.confirm_deploy__dialog_title))
      .setView(view)
      .setPositiveButton(coloredSpan(R.string.confirm_deploy__positive_button), { dialog, _ ->
        val passphrase = passphraseField.text.toString()
        if (passphrase.isBlank()) {
          passphraseInput.error = getString(R.string.confirm_deploy__empty_passphrase)
          return@setPositiveButton
        } else {
          passphraseInput.error = null
        }

        dialog.dismiss()
        onDeployConfirm(passphrase)
      })
      .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })
      .create()

    view.run {
      gasLimitWei = findViewById(R.id.confirm_deploy__gas_price_wei)
      gasPriceWei = findViewById(R.id.confirm_deploy__gas_limit_wei)
      passphraseInput = findViewById(R.id.confirm_deploy__passphrase_input)
    }

    gasLimitWei.text = contractGas.limit.toString()
    gasPriceWei.text = contractGas.price.toString()
    return dialog
  }

  override fun dismiss() {
    dialog.dismiss()
  }

  fun show() {
    show(fm, TAG)
  }

  companion object {
    private const val TAG = "io.sikorka.android.ui.contracts.deploy.ConfirmDeployDialog"
    fun create(
      fm: FragmentManager,
      gas: ContractGas,
      onDeployConfirm: OnDeployConfirm
    ): ConfirmDeployDialog {
      return ConfirmDeployDialog().apply {
        this.fm = fm
        this.onDeployConfirm = onDeployConfirm
        contractGas = gas
      }
    }
  }
}

typealias OnDeployConfirm = (passphrase: String) -> Unit