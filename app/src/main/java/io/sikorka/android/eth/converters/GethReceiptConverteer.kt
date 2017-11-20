package io.sikorka.android.eth.converters

import io.sikorka.android.eth.TransactionReceipt
import org.ethereum.geth.Receipt

class GethReceiptConverter: Converter<Receipt, TransactionReceipt> {
  override fun convert(from: Receipt): TransactionReceipt = TransactionReceipt(
      from.string().contains("status=1"),
      from.txHash.hex,
      from.contractAddress.hex
  )
}