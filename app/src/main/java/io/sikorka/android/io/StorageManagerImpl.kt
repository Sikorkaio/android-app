package io.sikorka.android.io

import android.app.Application
import io.sikorka.android.node.configuration.ConfigurationProvider
import org.apache.commons.io.FileUtils
import java.io.File
import javax.inject.Inject

class StorageManagerImpl
@Inject
constructor(
    private val application: Application,
    private val configurationProvider: ConfigurationProvider
) : StorageManager {

  override fun storageUsed(): Long {
    val active = configurationProvider.getActive()
    return FileUtils.sizeOf(active.dataDir)
  }

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

  companion object {
    private val REGISTRY_TRANSACTION_DIR = "registry_update_transactions"
  }
}