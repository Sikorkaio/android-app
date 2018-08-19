package io.sikorka.android.core.monitor

import io.sikorka.android.core.model.TransactionReceipt

typealias OnTransactionStatusUpdate = (txHash: String, status: Int) -> Unit

typealias statusUpdateListener = (receipt: TransactionReceipt) -> Unit