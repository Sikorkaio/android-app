package io.sikorka.android.node.monitor

import io.sikorka.android.eth.TransactionReceipt

typealias OnTransactionStatusUpdate = (txHash: String, status: Int) -> Unit

typealias OnDeploymentStatusUpdateListener = (receipt: TransactionReceipt) -> Unit