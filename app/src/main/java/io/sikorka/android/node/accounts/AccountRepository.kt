package io.sikorka.android.node.accounts

import io.reactivex.Observable
import io.reactivex.Single
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.helpers.Lce
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.all
import io.sikorka.android.node.toEther
import io.sikorka.android.settings.AppPreferences
import org.ethereum.geth.*
import timber.log.Timber
import javax.inject.Inject

class AccountRepository
@Inject constructor(
    @KeystorePath private val keystorePath: String,
    private val appPreferences: AppPreferences,
    private val gethNode: GethNode
) {

  private val keystore = KeyStore(keystorePath, Geth.LightScryptN, Geth.LightScryptP)

  fun createAccount(passphrase: String): Account {
    val newAccount = keystore.newAccount(passphrase)
    setDefault(newAccount.address.hex)
    return newAccount
  }

  fun accounts(): Observable<Lce<AccountsModel>> = Observable.fromCallable {
    val accounts = keystore.accounts.all()
    val accountsModel = AccountsModel(appPreferences.selectedAccount(), accounts.toList())
    return@fromCallable Lce.success(accountsModel)
  }.startWith(Lce.loading()).onErrorReturn { Lce.failure(it) }

  fun selectedAccount(): Single<AccountModel> = Single.fromCallable {
    val addressHex = appPreferences.selectedAccount()
    val account = getAccountByHex(addressHex)
    val balance = gethNode.getBalance(account.address)
    return@fromCallable AccountModel(addressHex, account, balance.toEther())
  }.onErrorReturn {
    val addressHex = appPreferences.selectedAccount()
    val account = getAccountByHex(addressHex)
    return@onErrorReturn AccountModel(addressHex, account)
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

  fun deleteAccount(account: Account, passphrase: String) {
    keystore.deleteAccount(account, passphrase)
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

}