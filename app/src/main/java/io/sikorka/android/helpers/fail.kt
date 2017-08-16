package io.sikorka.android.helpers

fun fail(message: String = ""): Nothing {
  throw RuntimeException(message)
}