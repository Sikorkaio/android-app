package io.sikorka.android.ui.accounts.account_import

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.view.MenuItem
import android.widget.ImageButton
import com.afollestad.materialdialogs.folderselector.FileChooserDialog
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult.CONFIRMATION_MISMATCH
import io.sikorka.android.core.accounts.ValidationResult.EMPTY_PASSPHRASE
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.accounts.account_import.AccountImportCodes.FAILED_TO_UNLOCK
import io.sikorka.android.ui.bind
import io.sikorka.android.ui.dialogs.selectFile
import io.sikorka.android.ui.setValue
import io.sikorka.android.ui.value
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieActivityModule
import java.io.File
import javax.inject.Inject

class AccountImportActivity : BaseActivity(),
  AccountImportView,
  FileChooserDialog.FileCallback {

  private val filePassphraseInput: TextInputLayout by bind(R.id.account_import__file_passphrase)
  private val accountPassphraseInput: TextInputLayout by bind(R.id.account_import__account_passphrase)
  private val accountPassphraseConfirmationInput: TextInputLayout by bind(R.id.account_import__account_passphrase_confirmation)
  private val filePathInput: TextInputLayout by bind(R.id.account_import__file_path)
  private val importAction: FloatingActionButton by bind(R.id.account_import__import_action)
  private val selectFileButton: ImageButton by bind(R.id.account_import__select_file_button)

  private val filePassphrase: String
    get() = filePassphraseInput.value()

  private val accountPassphrase: String
    get() = accountPassphraseInput.value()

  private val accountPassphraseConfirmation: String
    get() = accountPassphraseConfirmationInput.value()

  private val filePath: String
    get() = filePathInput.value()

  private lateinit var scope: Scope

  @Inject
  lateinit var presenter: AccountImportPresenter

  override fun onFileChooserDismissed(dialog: FileChooserDialog) {
    // do nothing
  }

  override fun onFileSelection(dialog: FileChooserDialog, file: File) {
    filePathInput.setValue(file.absolutePath)
  }

  override fun showError(code: Long) {
    accountPassphraseConfirmationInput.error = null
    accountPassphraseInput.error = null
    filePassphraseInput.error = null

    when (code) {
      CONFIRMATION_MISMATCH -> {
        val errorMessage = getString(R.string.account_import__confirmation_missmatch)
        accountPassphraseConfirmationInput.error = errorMessage
      }
      EMPTY_PASSPHRASE -> {
        accountPassphraseInput.error = getString(R.string.account_import__account_passphrase_empty)
      }
      FAILED_TO_UNLOCK -> {
        filePassphraseInput.error = getString(R.string.account_import__invalid_passphrase)
      }
      else -> {
        snackBar(R.string.account_import__unknown_error)
      }
    }
  }

  override fun importSuccess() {
    snackBar(R.string.account_import__import_success)
    finish()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(AccountImportModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieActivityModule(this))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account_import)
    Toothpick.inject(this, scope)
    setupToolbar(R.string.account_import__import_account_title)

    importAction.setOnClickListener {
      presenter.import(filePath, filePassphrase, accountPassphrase, accountPassphraseConfirmation)
    }
    selectFileButton.setOnClickListener { selectFile() }
    presenter.attach(this)
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    super.onDestroy()
  }

  @javax.inject.Scope
  @Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java

    fun start(context: Context) {
      val intent = Intent(context, AccountImportActivity::class.java)
      context.startActivity(intent)
    }
  }
}