package io.sikorka.android.ui.accounts.accountimport

import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.accounts.InvalidPassphraseException
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.ValidationResult
import io.sikorka.android.io.toByteArray
import io.sikorka.android.mvp.BasePresenter
import io.sikorka.android.ui.accounts.accountimport.AccountImportCodes.FAILED_TO_UNLOCK
import io.sikorka.android.ui.accounts.accountimport.AccountImportCodes.UNKNOWN_ERROR
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AccountImportActivity.Presenter
class AccountImportPresenterImpl
@Inject constructor(
  private val accountRepository: AccountRepository,
  private val passphraseValidator: PassphraseValidator
) :
  BasePresenter<AccountImportView>(),
  AccountImportPresenter {

  override fun import(
    filePath: String,
    filePassphrase: String,
    accountPassphrase: String,
    accountPassphraseConfirmation: String
  ) {

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

    disposables += accountRepository.importAccount(key, filePassphrase, accountPassphrase)
      .subscribe({ lce ->
        if (lce.success()) {
          attachedView().importSuccess()
        } else if (lce.failure()) {
          Timber.v(lce.error(), "Import error")

          when (lce.error()) {
            is InvalidPassphraseException -> {
              attachedView().showError(FAILED_TO_UNLOCK)
            }
            else -> {
              attachedView().showError(UNKNOWN_ERROR)
            }
          }
        }
      }) {
        Timber.e(it)
      }
  }
}