package io.sikorka.android.ui.accounts.account_export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.widget.ImageButton
import android.widget.TextView
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult.CONFIRMATION_MISMATCH
import io.sikorka.android.core.accounts.ValidationResult.EMPTY_PASSPHRASE
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.accounts.account_export.AccountExportCodes.ACCOUNT_PASSPHRASE_EMPTY
import io.sikorka.android.ui.accounts.account_export.AccountExportCodes.FAILED_TO_UNLOCK_ACCOUNT
import io.sikorka.android.ui.bind
import io.sikorka.android.ui.dialogs.selectDirectory
import io.sikorka.android.ui.value
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import java.io.File
import javax.inject.Inject

class AccountExportActivity : BaseActivity(),
  AccountExportView,
  FolderChooserDialog.FolderCallback {

  private val accountHex: TextView by bind(R.id.account_export__account_hex)

  private val exportPathInput: TextInputLayout by bind(R.id.account_export__path_input)

  private val accountPassphrase: TextInputLayout by bind(R.id.account_export__passphrase)

  private val encryptionPassphrase: TextInputLayout by bind(R.id.account_export__encryption_passphrase)

  private val encryptionPassphraseConfirmation: TextInputLayout by bind(R.id.account_export__encryption_passphrase_confirmation)

  private val accountExportFab: FloatingActionButton by bind(R.id.account_export__export_fab)

  private val selectDirectoryButton: ImageButton by bind(R.id.account_export__select_directory)

  private val account: String
    get() = accountHex.value()

  private val passphrase: String
    get() = accountPassphrase.value()

  private val encryptionPass: String
    get() = encryptionPassphrase.value()

  private val encryptionPassConfirmation: String
    get() = encryptionPassphraseConfirmation.value()

  private val exportPath: String
    get() = exportPathInput.value()

  private val hex: String
    get() = intent?.getStringExtra(ACCOUNT_HEX) ?: ""

  @Inject
  lateinit var presenter: AccountExportPresenter

  private fun clearErrors() {
    exportPathInput.error = null
    accountPassphrase.error = null
    encryptionPassphrase.error = null
    encryptionPassphraseConfirmation.error = null
  }

  override fun exportComplete() {
    Snackbar.make(accountHex, R.string.account_export__export_complete, Snackbar.LENGTH_SHORT)
    finish()
  }

  override fun showError(code: Long) {
    clearErrors()

    when (code) {
      CONFIRMATION_MISMATCH -> {
        encryptionPassphraseConfirmation.error =
            getString(R.string.account_export__confirmation_missmatch)
      }
      EMPTY_PASSPHRASE -> {
        encryptionPassphrase.error = getString(R.string.account_export__encryption_passphrase_empty)
      }
      FAILED_TO_UNLOCK_ACCOUNT -> {
        accountPassphrase.error = getString(R.string.account_export__failed_to_unlock)
      }
      ACCOUNT_PASSPHRASE_EMPTY -> {
        accountPassphrase.error = getString(R.string.account_export__passphrase_empty)
      }
      AccountExportCodes.INVALID_PASSPHRASE -> {
        accountPassphrase.error = getString(R.string.account_export__invalid_passphrase)
      }
      else -> {
        fail("Was not expecting $code")
      }
    }

  }

  override fun onFolderSelection(dialog: FolderChooserDialog, folder: File) {
    exportPathInput.editText?.setText(folder.absolutePath)
  }

  override fun onFolderChooserDismissed(dialog: FolderChooserDialog) {
    // do nothing?
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), AccountExportModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account_export)
    Toothpick.inject(this, scope)

    setupToolbar(R.string.account_export__export_account_title)

    accountExportFab.setOnClickListener {
      presenter.export(account, passphrase, encryptionPass, encryptionPassConfirmation, exportPath)
    }

    selectDirectoryButton.setOnClickListener { selectDirectory() }
    presenter.attach(this)
    accountHex.text = hex
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    super.onDestroy()
  }

  companion object {

    const val ACCOUNT_HEX = "io.sikorka.extra.ACCOUNT_HEX"

    fun start(context: Context, accountHex: String) {
      val intent = Intent(context, AccountExportActivity::class.java)
      intent.putExtra(ACCOUNT_HEX, accountHex)
      context.startActivity(intent)
    }
  }
}