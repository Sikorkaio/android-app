package io.sikorka.android.core.model

import io.sikorka.android.helpers.fail

data class TransactionReceipt(
  val successful: Boolean,
  val txHash: String,
  private val contractAddressHex: String? = null
) {
  /**
   * Returns a new transaction receipt with the [successful] and [txHash] of this transaction
   * but with the specified [contractAddressHex]. This should not be useful under normal
   * circumstances but it appears that when calling the "getTransactionReceipt" for
   * some reason the receipt has a null address (despite returning the proper address on a full
   * client). This means that when monitoring for contract creation we will have to inject
   * the contract address by hand.
   * @param contractAddressHex The created contract address
   * @return A transaction receipt.
   */
  fun withContractAddress(contractAddressHex: String): TransactionReceipt {
    return TransactionReceipt(this.successful, this.txHash, contractAddressHex)
  }

  fun contractAddress(): String {
    return contractAddressHex ?: fail("it seems that address was null.")
  }
}