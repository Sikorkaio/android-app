package io.sikorka.android.ui.accounts.account_creation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.core.accounts.ValidationResult.Code
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.asString
import toothpick.Toothpick
import javax.inject.Inject

class AccountCreationDialog : DialogFragment(), AccountCreationDialogView {

  private lateinit var passphraseField: EditText
  private lateinit var passphraseConfirmationField: EditText
  private lateinit var passphraseInput: TextInputLayout
  private lateinit var passphraseConfirmationInput: TextInputLayout

  @Inject
  lateinit var presenter: AccountCreationDialogPresenter

  private lateinit var dialog: MaterialDialog

  private val passphrase: String
    get() = passphraseField.asString()

  private val passphraseConfirmation: String
    get() = passphraseConfirmationField.asString()

  private lateinit var onDismissAction: (() -> Unit)
  private lateinit var fm: FragmentManager

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = context ?: fail("context was null")
    val scope = Toothpick.openScopes(context.applicationContext, context, this)
    scope.installModules(AccountCreationModule())
    Toothpick.inject(this, scope)

    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.dialog__account_create, null, false)
    view.run {
      passphraseField = findViewById(R.id.accounts__passphrase)
      passphraseConfirmationField = findViewById(R.id.accounts__passphrase_confirmation)
      passphraseInput = findViewById(R.id.accounts__passphrase_input)
      passphraseConfirmationInput = findViewById(R.id.accounts__passphrase_confirmation_input)
    }

    val builder = MaterialDialog.Builder(context)
        .title(R.string.account__create_account)
        .customView(view, false)
        .positiveText(android.R.string.ok)
        .negativeText(android.R.string.cancel)
        .dismissListener { presenter.detach() }
        .autoDismiss(false)
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

  override fun showError(@Code code: Int) {
    clearErrors()
    when (code) {
      ValidationResult.CONFIRMATION_MISMATCH -> {
        passphraseConfirmationInput.error = getString(R.string.account_creation__passphrase_missmatch)
      }
      ValidationResult.EMPTY_PASSPHRASE -> {
        passphraseInput.error = getString(R.string.account_creation__passphrase_empty)
      }
      ValidationResult.PASSWORD_SHORT -> {
        passphraseInput.error = getString(R.string.account_creation__password_short, 8)
      }
    }
  }

  override fun complete() {
    onDismissAction.invoke()
    dialog.dismiss()
  }

  private fun clearErrors() {
    passphraseInput.error = null
    passphraseConfirmationInput.error = null
  }

  fun show() {
    show(fm, TAG)
  }

  override fun dismiss() {
    dialog.dismiss()
  }

  companion object {
    const val TAG = "account_creation_dialog"

    fun newInstance(fm: FragmentManager, action: (() -> Unit)): AccountCreationDialog {
      val creationDialog = AccountCreationDialog()
      creationDialog.fm = fm
      creationDialog.onDismissAction = action
      return creationDialog
    }
  }
}