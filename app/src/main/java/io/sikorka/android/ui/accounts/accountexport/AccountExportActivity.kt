package io.sikorka.android.ui.accounts.accountexport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult.CONFIRMATION_MISMATCH
import io.sikorka.android.core.accounts.ValidationResult.EMPTY_PASSPHRASE
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.accounts.accountexport.AccountExportCodes.ACCOUNT_PASSPHRASE_EMPTY
import io.sikorka.android.ui.accounts.accountexport.AccountExportCodes.FAILED_TO_UNLOCK_ACCOUNT
import io.sikorka.android.ui.dialogs.fileSelectionDialog
import io.sikorka.android.ui.value
import kotterknife.bindView
import org.koin.android.ext.android.inject
import java.io.File

class AccountExportActivity : BaseActivity(), AccountExportView {

  private val account: TextView by bindView(R.id.account_export__account_hex)
  private val path: TextInputLayout by bindView(R.id.account_export__path_input)
  private val accountPassphrase: TextInputLayout by bindView(R.id.account_export__passphrase)
  private val accountExportFab: FloatingActionButton by bindView(R.id.account_export__export_fab)
  private val selectDirectoryButton: ImageButton by bindView(R.id.account_export__select_directory)
  private val filePassphrase: TextInputLayout by bindView(
    R.id.account_export__encryption_passphrase
  )
  private val filePassphraseConfirm: TextInputLayout by bindView(
    R.id.account_export__encryption_passphrase_confirmation
  )

  private val hex: String
    get() = intent?.getStringExtra(ACCOUNT_HEX) ?: ""

  private val presenter: AccountExportPresenter by inject()

  private fun clearErrors() {
    path.error = null
    accountPassphrase.error = null
    filePassphrase.error = null
    filePassphraseConfirm.error = null
  }

  override fun exportComplete() {
    Snackbar.make(account, R.string.account_export__export_complete, Snackbar.LENGTH_SHORT)
    finish()
  }

  override fun showError(code: Int) {
    clearErrors()

    when (code) {
      CONFIRMATION_MISMATCH -> {
        val errorMessage = getString(R.string.account_export__confirmation_missmatch)
        filePassphraseConfirm.error = errorMessage
      }
      EMPTY_PASSPHRASE -> {
        val errorMessage = getString(R.string.account_export__encryption_passphrase_empty)
        filePassphrase.error = errorMessage
      }
      FAILED_TO_UNLOCK_ACCOUNT -> {
        val errorMessage = getString(R.string.account_export__failed_to_unlock)
        accountPassphrase.error = errorMessage
      }
      ACCOUNT_PASSPHRASE_EMPTY -> {
        val errorMessage = getString(R.string.account_export__passphrase_empty)
        accountPassphrase.error = errorMessage
      }
      AccountExportCodes.INVALID_PASSPHRASE -> {
        val errorMessage = getString(R.string.account_export__invalid_passphrase)
        accountPassphrase.error = errorMessage
      }
      else -> {
        fail("Was not expecting $code")
      }
    }
  }

  private fun onFolderSelection(folder: File) {
    path.editText?.setText(folder.absolutePath)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account_export)
    setupToolbar(R.string.account_export__export_account_title)

    accountExportFab.setOnClickListener {
      presenter.export(
        account.value(),
        accountPassphrase.value(),
        filePassphrase.value(),
        filePassphraseConfirm.value(),
        path.value()
      )
    }

    selectDirectoryButton.setOnClickListener { _ ->
      fileSelectionDialog().show { onFolderSelection(it) }
    }
    presenter.attach(this)
    account.text = hex
  }

  override fun onDestroy() {
    presenter.detach()
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