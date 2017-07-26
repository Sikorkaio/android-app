package io.sikorka.test_geth.helpers

fun fail(message: String = ""): Nothing {
  throw RuntimeException(message)
}