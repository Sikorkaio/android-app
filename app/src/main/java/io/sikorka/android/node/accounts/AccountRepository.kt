package io.sikorka.android.node.accounts

import io.reactivex.Observable
import io.reactivex.Single
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.helpers.Lce
import org.ethereum.geth.Account
import org.ethereum.geth.Geth
import org.ethereum.geth.KeyStore
import javax.inject.Inject

class AccountRepository
@Inject constructor(@KeystorePath private val keystorePath: String) {

  private val keystore = KeyStore(keystorePath, Geth.LightScryptN, Geth.LightScryptP)

  fun createAccount(passphrase: String): Account {
    return keystore.newAccount(passphrase)
  }

  fun accounts(): Observable<Lce<List<Account>>> {
    return Observable.fromCallable {
      val accounts = keystore.accounts
      val accountList = (0 until accounts.size()).map { accounts[it] }
      return@fromCallable Lce.success(accountList)
    }.startWith(Lce.loading())
        .onErrorReturn { Lce.failure(it) }

  }

  fun exportAccount(account: Account, passphrase: String, keyPassphrase: String): Single<ByteArray> {
    return Single.fromCallable { keystore.exportKey(account, passphrase, keyPassphrase) }
  }

  fun deleteAccount(account: Account, passphrase: String) {
    keystore.deleteAccount(account, passphrase)
  }

  fun importAccount(key: ByteArray, keyPassphrase: String, passphrase: String) : Single<Account> {
    return Single.fromCallable { keystore.importKey(key, keyPassphrase, passphrase) }
  }

  fun changePassphrase(account: Account, oldPassphrase: String, newPassphrase: String) {
    keystore.updateAccount(account, oldPassphrase, newPassphrase)
  }


}