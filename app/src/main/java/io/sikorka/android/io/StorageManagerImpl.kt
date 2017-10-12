package io.sikorka.android.io

import android.app.Application
import java.io.File
import javax.inject.Inject

class StorageManagerImpl
@Inject
constructor(private val application: Application) : StorageManager {

  private fun getTransactionDirectory(): File {
    val filesDir = application.filesDir
    val registryUpdateTrasactionsDir = File(filesDir, REGISTRY_TRANSACTION_DIR)
    if (!registryUpdateTrasactionsDir.exists()) {
      val created = registryUpdateTrasactionsDir.mkdir()

      if (!created) {
        throw RuntimeException("folder creation was not successful")
      }
    }
    return registryUpdateTrasactionsDir
  }

  override fun registryTransactionFile(contractAddress: String): File {
    val transactionDirectory = getTransactionDirectory()
    return File(transactionDirectory, contractAddress)
  }

  companion object {
    private val REGISTRY_TRANSACTION_DIR = "registry_update_transactions"
  }
}