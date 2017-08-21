package io.sikorka.android.ui.accounts.account_import

import io.sikorka.android.io.toByteArray
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.node.accounts.AccountRepository
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.ValidationResult
import org.ethereum.geth.Account
import java.io.File
import javax.inject.Inject


class AccountImportPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator
) :
    BasePresenter<AccountImportView>(),
    AccountImportPresenter {

  override fun import(filePath: String, filePassphrase: String, accountPassphrase: String, accountPassphraseConfirmation: String) {

    val file = File(filePath)

    if (!file.exists()) {
      attachedView().showError(AccountImportCodes.FILE_DOES_NOT_EXIST)
      return
    }

    val code = passphraseValidator.validate(accountPassphrase, accountPassphraseConfirmation)

    if (code != ValidationResult.OK) {
      attachedView().showError(code)
      return
    }

    val key = file.toByteArray()

    accountRepository.importAccount(key, filePassphrase, accountPassphrase).subscribe({ t: Account? ->
     if (t == null) {
       attachedView().showError(AccountImportCodes.FAILED_TO_UNLOCK)
     } else {
       attachedView().importSuccess()
     }
    }) {

    }
  }

}
