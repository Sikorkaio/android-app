package io.sikorka.android.core.accounts

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.sikorka.android.core.accounts.AccountModel.Companion.NO_BALANCE
import io.sikorka.android.core.all
import io.sikorka.android.core.model.Address
import io.sikorka.android.core.model.converters.GethAccountConverter
import io.sikorka.android.core.model.converters.GethAddressConverter
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.helpers.Lce
import io.sikorka.android.settings.AppPreferences
import org.ethereum.geth.Account
import org.ethereum.geth.BigInt
import org.ethereum.geth.Geth
import org.ethereum.geth.KeyStore
import org.ethereum.geth.Transaction
import timber.log.Timber
import javax.inject.Inject
import io.sikorka.android.core.model.Account as SikorkaAccount

class AccountRepository
@Inject constructor(
  @KeystorePath private val keystorePath: String,
  private val appPreferences: AppPreferences,
  private val accountBalanceDao: AccountBalanceDao
) {

  private val accountConverter: GethAccountConverter = GethAccountConverter()
  private val addressConverter: GethAddressConverter = GethAddressConverter()

  private val keystore = KeyStore(keystorePath, Geth.LightScryptN, Geth.LightScryptP)

  fun createAccount(passphrase: String): Single<Account> = Single.fromCallable {
    val newAccount = keystore.newAccount(passphrase)
    setDefault(newAccount.address.hex)
    return@fromCallable newAccount
  }

  fun accounts(): Observable<Lce<AccountsModel>> = Observable.fromCallable {
    val accounts = keystore.accounts.all().map { accountConverter.convert(it) }
    val accountsModel = AccountsModel(appPreferences.selectedAccount(), accounts.toList())
    return@fromCallable Lce.success(accountsModel)
  }.startWith(Lce.loading()).onErrorReturn { Lce.failure(it) }

  fun selectedAccount(): Single<AccountModel> = Single.fromCallable {
    val addressHex = appPreferences.selectedAccount()
    val balance = accountBalanceDao.getBalance(addressHex)
    return@fromCallable AccountModel(addressHex, balance?.balance ?: NO_BALANCE)
  }.onErrorReturn {
      val addressHex = appPreferences.selectedAccount()
      return@onErrorReturn AccountModel(addressHex)
    }

  private fun getAccountByHex(addressHex: String): Account {
    return keystore.accounts.all().first { it.address.hex.equals(addressHex, ignoreCase = true) }
  }

  fun export(addressHex: String, passphrase: String, keyPassphrase: String): Single<ByteArray> {
    return Single.fromCallable {
      keystore.exportKey(getAccountByHex(addressHex), passphrase, keyPassphrase)
    }.onErrorResumeNext { Single.error(checkIfInvalidPassphrase(it)) }
  }

  private fun checkIfInvalidPassphrase(throwable: Throwable): Throwable {
    val message = throwable.message.orEmpty()
    return if (message.contains(INVALID_PASSPHRASE)) {
      InvalidPassphraseException(throwable)
    } else {
      throwable
    }
  }

  fun deleteAccount(account: SikorkaAccount, passphrase: String): Completable {
    return Completable.fromCallable {
      val matchingAccount = keystore.accounts
        .all()
        .first { it.address.hex == account.address.hex }

      keystore.deleteAccount(matchingAccount, passphrase)
      accountBalanceDao.deleteByHex(account.address.hex)
    }.onErrorResumeNext { Completable.error(checkIfInvalidPassphrase(it)) }
  }

  fun importAccount(
    key: ByteArray,
    keyPassphrase: String,
    passphrase: String
  ): Single<Lce<SikorkaAccount>> {
    return Single.fromCallable {
      val account = keystore.importKey(key, keyPassphrase, passphrase)
      setDefault(account.address.hex)

      return@fromCallable if (account == null) {
        Lce.failure(InvalidPassphraseException())
      } else {
        Lce.success(accountConverter.convert(account))
      }
    }.onErrorReturn { Lce.failure(checkIfInvalidPassphrase(it)) }
  }

  private fun setDefault(accountHex: String) {
    if (appPreferences.selectedAccount().isBlank()) {
      appPreferences.selectAccount(accountHex)
    }
  }

  fun changePassphrase(account: Account, oldPassphrase: String, newPassphrase: String) {
    keystore.updateAccount(account, oldPassphrase, newPassphrase)
  }

  fun sign(
    address: String,
    passphrase: String,
    transaction: Transaction,
    chainId: BigInt
  ): Transaction? {
    val account = keystore.accounts.all()
      .first { it.address.hex.equals(address, ignoreCase = true) }
    val hex = account.address.hex
    Timber.v("Signing $hex - ${transaction.hash.hex} - chain: ${chainId.int64}")
    return keystore.signTxPassphrase(account, passphrase, transaction, chainId)
  }

  fun accountsExist(): Single<Boolean> = Single.fromCallable { keystore.accounts.size() > 0 }

  fun setDefaultAccount(account: SikorkaAccount): Completable = Completable.fromCallable {
    val accountHex = account.address.hex
    appPreferences.selectAccount(accountHex)
  }

  fun getAccountAddresses(): Observable<Address> = keystore.accounts.all()
    .map { addressConverter.convert(it.address) }
    .toObservable()

  fun observeDefaultAccountBalance(): LiveData<AccountBalance> {
    return accountBalanceDao.observeBalance(appPreferences.selectedAccount())
  }

  companion object {
    private const val INVALID_PASSPHRASE = "could not decrypt key with given passphrase"
  }
}