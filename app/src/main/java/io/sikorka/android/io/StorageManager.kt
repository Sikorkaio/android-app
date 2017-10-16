package io.sikorka.android.io

import java.io.File

interface StorageManager {

  /**
   * Creates a new file in the application storage to store a signed transaction that will
   * update the registry contract with the address and location of the deployed SikorkaInterface
   * contract.
   *
   * @param contractAddress the
   * @return A file where the pending transaction will be written.
   */
  fun registryTransactionFile(contractAddress: String): File

  fun storageUsed(): Long
}