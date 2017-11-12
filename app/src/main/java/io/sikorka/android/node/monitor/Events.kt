package io.sikorka.android.node.monitor

data class PrepareTransactionStatusEvent(val txHash: String, val success: Boolean)

data class TransactionStatusEvent(val txHash: String, val success: Boolean)

data class ContractStatusEvent(val address: String, val txHash: String, val success: Boolean)