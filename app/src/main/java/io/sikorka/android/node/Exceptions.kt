package io.sikorka.android.node

class ExceedsBlockGasLimit(cause: Throwable) : Exception(cause)

class NoSuitablePeersAvailableException(cause: Throwable) : Exception(cause)

class TransactionNotFoundException(hash: String, cause: Throwable) : Exception("Transcation $hash not found", cause)