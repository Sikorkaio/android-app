package io.sikorka.test_geth.accounts

import io.sikorka.test_geth.di.qualifiers.KeystorePath
import org.ethereum.geth.Account
import org.ethereum.geth.Geth
import org.ethereum.geth.KeyStore
import javax.inject.Inject

class KeystoreManager
@Inject constructor(@KeystorePath private val keystorePath: String) {

  private val keystore = KeyStore(keystorePath, Geth.LightScryptN, Geth.LightScryptP)

  fun createAccount(passphrase: String): Account {
    return keystore.newAccount(passphrase)
  }

  fun accounts(): List<Account> {
    val accounts = keystore.accounts
    val accountList = (0 until accounts.size()).map { accounts[it] }
    return accountList
  }



}