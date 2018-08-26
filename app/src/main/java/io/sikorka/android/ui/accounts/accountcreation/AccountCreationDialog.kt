package io.sikorka.android.ui.accounts.accountcreation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.core.accounts.ValidationResult.Code
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.asString
import io.sikorka.android.ui.coloredSpan
import toothpick.Toothpick
import javax.inject.Inject

class AccountCreationDialog : androidx.fragment.app.DialogFragment(), AccountCreationDialogView {

  private lateinit var passphraseField: EditText
  private lateinit var passphraseConfirmationField: EditText
  private lateinit var passphraseInput: com.google.android.material.textfield.TextInputLayout
  private lateinit var passphraseConfirmationInput: com.google.android.material.textfield.TextInputLayout

  @Inject
  lateinit var presenter: AccountCreationDialogPresenter

  private lateinit var dialog: AlertDialog

  private val passphrase: String
    get() = passphraseField.asString()

  private val passphraseConfirmation: String
    get() = passphraseConfirmationField.asString()

  private lateinit var onDismissAction: (() -> Unit)
  private lateinit var fm: androidx.fragment.app.FragmentManager

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

    val builder = AlertDialog.Builder(context)
      .setTitle(coloredSpan(R.string.account__create_account))
      .setView(view)
      .setCancelable(false)
      .setOnDismissListener { presenter.detach() }
      .setPositiveButton(coloredSpan(android.R.string.ok), { _, _ ->
        presenter.createAccount(passphrase, passphraseConfirmation)
      })
      .setNegativeButton(coloredSpan(android.R.string.cancel), { dialog, _ -> dialog.dismiss() })

    dialog = builder.create()
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
        val errorMessage = getString(R.string.account_creation__passphrase_missmatch)
        passphraseConfirmationInput.error = errorMessage
      }
      ValidationResult.EMPTY_PASSPHRASE -> {
        val errorMessage = getString(R.string.account_creation__passphrase_empty)
        passphraseInput.error = errorMessage
      }
      ValidationResult.PASSWORD_SHORT -> {
        val errorMessage = getString(R.string.account_creation__password_short, 8)
        passphraseInput.error = errorMessage
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

    fun newInstance(fm: androidx.fragment.app.FragmentManager, action: (() -> Unit)): AccountCreationDialog {
      val creationDialog = AccountCreationDialog()
      creationDialog.fm = fm
      creationDialog.onDismissAction = action
      return creationDialog
    }
  }
}