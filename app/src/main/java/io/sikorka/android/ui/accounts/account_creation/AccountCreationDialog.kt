package io.sikorka.android.ui.accounts.account_creation

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
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.sikorka.android.R
import io.sikorka.android.node.accounts.ValidationResult
import io.sikorka.android.node.accounts.ValidationResult.Code
import io.sikorka.android.ui.asString
import timber.log.Timber
import toothpick.Toothpick
import toothpick.smoothie.provider.SupportFragmentManagerProvider
import java.util.concurrent.TimeUnit
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
    get() = passphraseField.asString()

  private val passphraseConfirmation: String
    get() = passphraseConfirmationField.asString()

  private lateinit var onDismissAction: (() -> Unit)
  private lateinit var fm: FragmentManager

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

  override fun showError(@Code code: Long) {
    clearErrors()
    when (code) {
      ValidationResult.CONFIRMATION_MISSMATCH -> {
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