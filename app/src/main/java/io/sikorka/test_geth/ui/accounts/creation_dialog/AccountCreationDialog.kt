package io.sikorka.test_geth.ui.accounts.creation_dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.test_geth.R
import toothpick.Toothpick
import javax.inject.Inject

class AccountCreationDialog : DialogFragment(), AccountCreationDialogView {

  @BindView(R.id.accounts__passphrase)
  internal lateinit var passphraseField: EditText
  @BindView(R.id.accounts__passphrase_confirmation)
  internal lateinit var passphraseConfirmationField: EditText
  @BindView(R.id.accounts__passphrase_input)
  internal lateinit var passphraseInput: TextInputLayout
  @BindView(R.id.accounts__passphrase_confirmation_input)
  internal lateinit var passphraseConfirmationInput: TextInputLayout

  @Inject
  internal lateinit var presenter: AccountCreationDialogPresenter

  private lateinit var dialog: MaterialDialog

  private val passphrase: String
    get() = passphraseField.text.toString()

  private val passphraseConfirmation: String
    get() = passphraseConfirmationField.text.toString()

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val scope = Toothpick.openScopes(context.applicationContext, context, this)
    scope.installModules(AccountCreationModule())
    Toothpick.inject(this, scope)

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__account_create, null, false)
    ButterKnife.bind(this, view)

    val builder = MaterialDialog.Builder(context)
        .title(R.string.account__create_account)
        .customView(view, false)
        .positiveText(android.R.string.ok)
        .negativeText(android.R.string.cancel)
        .onNegative { dialog, _ -> dialog.dismiss() }
        .onPositive { _, _ ->
          presenter.createAccount(passphrase, passphraseConfirmation)
        }

    dialog = builder.build()
    return dialog
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun showError(@AccountCreationCodes.ErrorCode code: Long) {
    clearErrors()
    when (code) {
      AccountCreationCodes.CONFIRMATION_MISSMATCH -> {
        passphraseConfirmationInput.error = getString(R.string.account_creation__passphrase_missmatch)
      }
      AccountCreationCodes.EMPTY_PASSPHRASE -> {
        passphraseInput.error = getString(R.string.account_creation__passphrase_empty)
      }
    }
  }

  override fun complete() {
    dialog.dismiss()
  }

  private fun clearErrors() {
    passphraseInput.error = null
    passphraseConfirmationInput.error = null
  }

  fun show(fragmentManager: FragmentManager) {
    show(fragmentManager, TAG)
  }

  override fun dismiss() {
    dialog.dismiss()
  }

  companion object {
    const val TAG = "account_creation_dialog"
  }
}