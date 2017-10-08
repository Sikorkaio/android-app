package io.sikorka.android.ui.accounts.account_export

import io.sikorka.android.helpers.fail
import io.sikorka.android.io.toFile
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.ValidationResult
import java.io.File
import javax.inject.Inject

class AccountExportPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator
)
  : AccountExportPresenter, BasePresenter<AccountExportView>() {
  override fun export(accountHex: String, passphrase: String, encryptionPass: String, confirmation: String, path: String) {
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

    val code = passphraseValidator.validate(passphrase, confirmation)
    if (code != ValidationResult.OK) {
      attachedView().showError(code)
      return
    }

    accountRepository.accountByHex(accountHex)
        .flatMap { account -> accountRepository.exportAccount(account, passphrase, encryptionPass) }
        .subscribe({
          if (it.isEmpty()) {
            attachedView().showError(AccountExportCodes.FAILED_TO_UNLOCK_ACCOUNT)
          } else {
            it.toFile(File(exportDirectory, accountHex))
            attachedView().exportComplete()
          }
        }) {
          attachedView().showError(92L)
        }
  }
}