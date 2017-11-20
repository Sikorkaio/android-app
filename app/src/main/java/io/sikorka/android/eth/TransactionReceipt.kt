package io.sikorka.android.eth

data class TransactionReceipt(
    val successful: Boolean,
    val txHash: String,
    val contractAddressHex: String
)