package io.sikorka.android.ui.accounts.accountimport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import io.sikorka.android.R
import io.sikorka.android.core.accounts.ValidationResult.CONFIRMATION_MISMATCH
import io.sikorka.android.core.accounts.ValidationResult.EMPTY_PASSPHRASE
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.accounts.accountimport.AccountImportCodes.FAILED_TO_UNLOCK
import io.sikorka.android.ui.dialogs.fileSelectionDialog
import io.sikorka.android.ui.setValue
import io.sikorka.android.ui.value
import kotterknife.bindView
import org.koin.android.ext.android.inject
import java.io.File

class AccountImportActivity : BaseActivity(), AccountImportView {

  private val filePassphrase: TextInputLayout by bindView(R.id.account_import__file_passphrase)
  private val filePath: TextInputLayout by bindView(R.id.account_import__file_path)
  private val importAction: FloatingActionButton by bindView(R.id.account_import__import_action)
  private val selectFileButton: ImageButton by bindView(R.id.account_import__select_file_button)
  private val accountPassphrase: TextInputLayout by bindView(
    R.id.account_import__account_passphrase
  )
  private val accountPassphraseConfirmation: TextInputLayout by bindView(
    R.id.account_import__account_passphrase_confirmation
  )

  private val presenter: AccountImportPresenter by inject()

  private fun onFileSelection(file: File) {
    filePath.setValue(file.absolutePath)
  }

  override fun showError(code: Int) {
    accountPassphraseConfirmation.error = null
    accountPassphrase.error = null
    filePassphrase.error = null

    when (code) {
      CONFIRMATION_MISMATCH -> {
        val errorMessage = getString(R.string.account_import__confirmation_missmatch)
        accountPassphraseConfirmation.error = errorMessage
      }
      EMPTY_PASSPHRASE -> {
        accountPassphrase.error = getString(R.string.account_import__account_passphrase_empty)
      }
      FAILED_TO_UNLOCK -> {
        filePassphrase.error = getString(R.string.account_import__invalid_passphrase)
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
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account_import)

    setupToolbar(R.string.account_import__import_account_title)

    importAction.setOnClickListener {
      presenter.import(
        filePath.value(),
        filePassphrase.value(),
        accountPassphrase.value(),
        accountPassphraseConfirmation.value()
      )
    }
    selectFileButton.setOnClickListener { _ ->
      val dialog = fileSelectionDialog(true)
      dialog.show { onFileSelection(it) }
    }
    presenter.attach(this)
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, AccountImportActivity::class.java)
      context.startActivity(intent)
    }
  }
}