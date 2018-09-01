package io.sikorka.android.ui.accounts.accountexport

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.accounts.InvalidPassphraseException
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.helpers.fail
import io.sikorka.android.io.toFile
import io.sikorka.android.mvp.BasePresenter
import java.io.File

class AccountExportPresenterImpl(
  private val accountRepository: AccountRepository,
  private val passphraseValidator: PassphraseValidator
) : AccountExportPresenter, BasePresenter<AccountExportView>() {
  override fun export(
    accountHex: String,
    passphrase: String,
    encryptionPass: String,
    confirmation: String,
    path: String
  ) {
    val exportDirectory = File(path)

    if (!exportDirectory.exists()) {
      exportDirectory.mkdir()
    }

    if (accountHex.isBlank()) {
      fail("Account should never be blank")
    }

    if (passphrase.isBlank()) {
      attachedView().showError(AccountExportCodes.ACCOUNT_PASSPHRASE_EMPTY)
      return
    }

    val code = passphraseValidator.validate(encryptionPass, confirmation)
    if (code != ValidationResult.OK) {
      attachedView().showError(code)
      return
    }

    disposables += accountRepository.export(accountHex, passphrase, encryptionPass)
      .subscribe({
        if (it.isEmpty()) {
          attachedView().showError(AccountExportCodes.FAILED_TO_UNLOCK_ACCOUNT)
        } else {
          it.toFile(File(exportDirectory, accountHex))
          attachedView().exportComplete()
        }
      }) {
        if (it is InvalidPassphraseException) {
          attachedView().showError(AccountExportCodes.INVALID_PASSPHRASE)
        } else {
          attachedView().showError(92)
        }
      }
  }
}