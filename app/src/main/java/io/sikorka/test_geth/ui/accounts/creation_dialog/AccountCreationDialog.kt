package io.sikorka.test_geth.ui.accounts.creation_dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.test_geth.R

class AccountCreationDialog : DialogFragment() {

  @BindView(R.id.accounts__passphrase)
  internal lateinit var passphraseField: EditText
  @BindView(R.id.accounts__passphrase_confirmation)
  internal lateinit var passphraseConfirmationField: EditText

  private val passphrase: String
    get() = passphraseField.text.toString()

  private val passphraseConfirmation: String
    get() = passphraseConfirmationField.text.toString()

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__account_create, null, false)
    ButterKnife.bind(this, view)

    MaterialDialog.Builder(context)
        .title(R.string.account__create_account)
        .customView(view, false)
        .positiveText(android.R.string.ok)
        .negativeText(android.R.string.cancel)
        .onNegative { dialog, _ -> dialog.dismiss() }
        .onPositive { dialog, _ ->
          if (passphrase == passphraseConfirmation) {

          } else {

          }
        }

  }
}