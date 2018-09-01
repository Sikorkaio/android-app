package io.sikorka.android.io

import android.content.Context
import android.os.StatFs
import io.sikorka.android.core.configuration.ConfigurationProvider
import java.io.File

class StorageManagerImpl(
  private val context: Context,
  private val configurationProvider: ConfigurationProvider
) : StorageManager {

  override fun storageUsed(): Long {
    val active = configurationProvider.getActive()
    val statFs = StatFs(active.dataDir.absolutePath)
    return statFs.blockCountLong
  }

  private fun getTransactionDirectory(): File {
    val filesDir = context.filesDir
    val registryUpdateTrasactionsDir = File(filesDir, REGISTRY_TRANSACTION_DIR)
    if (!registryUpdateTrasactionsDir.exists()) {
      val created = registryUpdateTrasactionsDir.mkdir()

      if (!created) {
        throw RuntimeException("folder creation was not successful")
      }
    }
    return registryUpdateTrasactionsDir
  }

  companion object {
    private const val REGISTRY_TRANSACTION_DIR = "registry_update_transactions"
  }
}