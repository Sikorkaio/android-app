package io.sikorka.android.node.monitor

typealias OnTransactionStatusUpdate = (txHash: String, status: Int) -> Unit

typealias OnDeploymentStatusUpdateListener = (status: Boolean, contractAddress: String, txHash: String) -> Unit