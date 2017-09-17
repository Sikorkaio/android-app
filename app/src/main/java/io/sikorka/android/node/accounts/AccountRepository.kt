package io.sikorka.android.node.accounts

import io.reactivex.Observable
import io.reactivex.Single
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.helpers.Lce
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.toEther
import io.sikorka.android.settings.AppPreferences
import org.ethereum.geth.Account
import org.ethereum.geth.Geth
import org.ethereum.geth.KeyStore
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

  fun accounts(): Observable<Lce<List<Account>>> {
    return Observable.fromCallable {
      val accounts = keystore.accounts
      val accountList = (0 until accounts.size()).map { accounts[it] }
      return@fromCallable Lce.success(accountList)
    }.startWith(Lce.loading())
        .onErrorReturn { Lce.failure(it) }

  }

  fun selectedAccount(): Single<AccountModel> = Single.fromCallable {
    val account = appPreferences.selectedAccount()
    val address = Geth.newAddressFromHex(account)
    val balance = gethNode.getBalance(address)
    return@fromCallable AccountModel(account, balance.toEther())
  }.onErrorReturn {
    val account = appPreferences.selectedAccount()
    return@onErrorReturn AccountModel(account)
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


}