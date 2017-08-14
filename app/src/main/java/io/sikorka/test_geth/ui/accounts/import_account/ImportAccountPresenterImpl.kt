package io.sikorka.test_geth.ui.accounts.import_account

import io.sikorka.test_geth.accounts.AccountRepository
import io.sikorka.test_geth.accounts.PassphraseValidator
import io.sikorka.test_geth.accounts.ValidationResult
import io.sikorka.test_geth.io.toByteArray
import io.sikorka.test_geth.mvp.BasePresenter
import org.ethereum.geth.Account
import java.io.File
import javax.inject.Inject


class ImportAccountPresenterImpl
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val passphraseValidator: PassphraseValidator
) :
    BasePresenter<ImportAccountView>(),
    ImportAccountPresenter {

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
