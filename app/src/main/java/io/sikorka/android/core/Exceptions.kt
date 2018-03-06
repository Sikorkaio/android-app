package io.sikorka.android.core

class ExceedsBlockGasLimit(cause: Throwable) : Exception(cause)

class NoSuitablePeersAvailableException(cause: Throwable) : Exception(cause)

class TransactionNotFoundException(hash: String, cause: Throwable)
  : Exception("Transcation $hash not found", cause)

class NoContractCodeAtGivenAddressException(cause: Throwable) : Exception(cause)

val Throwable?.messageValue: String
  get() = this?.message ?: ""