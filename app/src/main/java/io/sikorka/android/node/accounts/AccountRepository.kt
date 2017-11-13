package io.sikorka.android.node.accounts

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.eth.Address
import io.sikorka.android.eth.converters.GethAccountConverter
import io.sikorka.android.eth.converters.GethAddressConverter
import io.sikorka.android.helpers.Lce
import io.sikorka.android.node.all
import io.sikorka.android.node.ethereumclient.LightClientProvider
import io.sikorka.android.node.toEther
import io.sikorka.android.settings.AppPreferences
import org.ethereum.geth.*
import timber.log.Timber
import javax.inject.Inject
import io.sikorka.android.eth.Account as SikorkaAccount

class AccountRepository
@Inject constructor(
    @KeystorePath private val keystorePath: String,
    private val appPreferences: AppPreferences,
    private val lightClientProvider: LightClientProvider
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
    val account = getAccountByHex(addressHex)
    val balance = lightClientProvider.get().getBalance(Address(hex = addressHex))
    return@fromCallable AccountModel(addressHex, accountConverter.convert(account), balance.toEther())
  }.onErrorReturn {
    val addressHex = appPreferences.selectedAccount()
    val account = getAccountByHex(addressHex)
    return@onErrorReturn AccountModel(addressHex, accountConverter.convert(account))
  }

  private fun getAccountByHex(addressHex: String): Account {
    return keystore.accounts.all().first { it.address.hex.equals(addressHex, ignoreCase = true) }
  }

  fun accountByHex(addressHex: String): Single<Account> = Single.fromCallable {
    return@fromCallable getAccountByHex(addressHex)
  }

  fun exportAccount(account: Account, passphrase: String, keyPassphrase: String): Single<ByteArray> {
    return Single.fromCallable { keystore.exportKey(account, passphrase, keyPassphrase) }
  }

  fun deleteAccount(account: SikorkaAccount, passphrase: String): Completable = Completable.fromCallable {
    val matchingAccount = keystore.accounts.all().first { it.address.hex == account.address.hex }
    keystore.deleteAccount(matchingAccount, passphrase)
  }.onErrorResumeNext {
    val message = it.message ?: ""
    if (message.contains("could not decrypt key with given passphrase")) {
      return@onErrorResumeNext Completable.error(InvalidPassphraseException(it))
    } else {
      return@onErrorResumeNext Completable.error(it)
    }
  }

  fun importAccount(key: ByteArray, keyPassphrase: String, passphrase: String): Single<Account> {
    return Single.fromCallable {
      val account = keystore.importKey(key, keyPassphrase, passphrase)
      setDefault(account.address.hex)
      account
    }
  }

  private fun setDefault(accountHex: String) {
    if (appPreferences.selectedAccount().isBlank()) {
      appPreferences.selectAccount(accountHex)
    }
  }

  fun changePassphrase(account: Account, oldPassphrase: String, newPassphrase: String) {
    keystore.updateAccount(account, oldPassphrase, newPassphrase)
  }

  fun sign(address: String, passphrase: String, transaction: Transaction, chainId: BigInt): Transaction? {
    val account = keystore.accounts.all()
        .first { it.address.hex.equals(address, ignoreCase = true) }
    Timber.v("Signing ${account.address.hex} - ${transaction.hash.hex} - chain: ${chainId.int64}")
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

}