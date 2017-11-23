package io.sikorka.android.ui.contracts.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.widget.EditText
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.android.R
import io.sikorka.android.core.contracts.data.ContractGas
import io.sikorka.android.helpers.fail

class ConfirmDeployDialog : DialogFragment() {

  private lateinit var gasPriceWei: TextView
  private lateinit var gasLimitWei: TextView
  private lateinit var passphraseInput: TextInputLayout

  private lateinit var dialog: MaterialDialog

  private lateinit var fm: FragmentManager
  private lateinit var onDeployConfirm: OnDeployConfirm
  private lateinit var contractGas: ContractGas

  private val passphraseField: EditText
    get() = passphraseInput.editText ?: fail("passphraseInput edittext was null")

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = context ?: fail("null context")
    dialog = MaterialDialog.Builder(context)
        .title(R.string.confirm_deploy__dialog_title)
        .titleColorRes(R.color.colorAccent)
        .positiveText(R.string.confirm_deploy__positive_button)
        .negativeText(android.R.string.cancel)
        .customView(R.layout.dialog__confirm_deploy, false)
        .onPositive { dialog, which ->
          val passphrase = passphraseField.text.toString()
          if (passphrase.isBlank()) {
            passphraseInput.error = getString(R.string.confirm_deploy__empty_passphrase)
            return@onPositive
          } else {
            passphraseInput.error = null
          }

          dialog.dismiss()
          onDeployConfirm(passphrase)
        }
        .build()

    dialog.view.run {
      gasLimitWei = findViewById(R.id.confirm_deploy__gas_price_wei)
      gasLimitWei = findViewById(R.id.confirm_deploy__gas_limit_wei)
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
    fun create(fm: FragmentManager, gas: ContractGas, onDeployConfirm: OnDeployConfirm): ConfirmDeployDialog {
      return ConfirmDeployDialog().apply {
        this.fm = fm
        this.onDeployConfirm = onDeployConfirm
        contractGas = gas
      }
    }
  }
}

typealias OnDeployConfirm = (passphrase: String) -> Unit