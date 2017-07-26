package io.sikorka.test_geth.accounts

import io.reactivex.Observable
import io.sikorka.test_geth.di.qualifiers.KeystorePath
import io.sikorka.test_geth.helpers.Lce
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
}