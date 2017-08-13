package io.sikorka.test_geth.ui.accounts.export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog
import io.sikorka.test_geth.R
import io.sikorka.test_geth.accounts.ValidationResult
import io.sikorka.test_geth.helpers.fail
import io.sikorka.test_geth.ui.dialogs.selectDirectory
import io.sikorka.test_geth.ui.value
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import java.io.File
import javax.inject.Inject

class AccountExportActivity : AppCompatActivity(),
    AccountExportView,
    FolderChooserDialog.FolderCallback {

  @BindView(R.id.account_export__account_hex)
  internal lateinit var accountHex: TextView

  @BindView(R.id.account_export__path_input)
  internal lateinit var exportPathInput: TextInputLayout

  @BindView(R.id.account_export__passphrase)
  internal lateinit var accountPassphrase: TextInputLayout

  @BindView(R.id.account_export__encryption_passphrase)
  internal lateinit var encryptionPassphrase: TextInputLayout

  @BindView(R.id.account_export__encryption_passphrase_confirmation)
  internal lateinit var encryptionPassphraseConfirmation: TextInputLayout


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

  @Inject internal lateinit var presenter: AccountExportPresenter


  @OnClick(R.id.account_export__select_directory)
  internal fun onSelectDirectory() {
    selectDirectory()
  }

  @OnClick(R.id.account_export__export_fab)
  internal fun onExport() {
    presenter.export(account, passphrase, encryptionPass, encryptionPassConfirmation, exportPath)
  }

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
      ValidationResult.CONFIRMATION_MISSMATCH -> {
        encryptionPassphraseConfirmation.error = getString(R.string.account_export__confirmation_missmatch)
      }
      ValidationResult.EMPTY_PASSPHRASE -> {
        encryptionPassphrase.error = getString(R.string.account_export__encryption_passphrase_empty)
      }
      AccountExportCodes.FAILED_TO_UNLOCK_ACCOUNT -> {
        accountPassphrase.error = getString(R.string.account_export__failed_to_unlock)
      }
      AccountExportCodes.ACCOUNT_PASSPHRASE_EMPTY -> {
        accountPassphrase.error = getString(R.string.account_export__passphrase_empty)
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
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account_export)
    ButterKnife.bind(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    accountHex.text = hex
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
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