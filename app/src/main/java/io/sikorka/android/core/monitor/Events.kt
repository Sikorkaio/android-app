package io.sikorka.android.core.monitor

data class PrepareTransactionStatus(val txHash: String, val success: Boolean)

data class TransactionStatus(val txHash: String, val success: Boolean)

data class ContractStatus(val address: String, val txHash: String, val success: Boolean)