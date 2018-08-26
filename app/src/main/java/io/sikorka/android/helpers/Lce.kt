package io.sikorka.android.helpers

import androidx.annotation.VisibleForTesting

class Lce<out T>(
  private val loading: Boolean,
  private val error: Throwable? = null,
  private val data: T? = null
) {

  fun loading(): Boolean = loading

  fun success(): Boolean = data != null

  fun failure(): Boolean = error != null

  fun data(): T = this.data ?: fail("data was null")

  fun error(): Throwable = this.error ?: fail("throwable was null")

  @VisibleForTesting
  fun errorMessage(): String {
    return error?.message ?: "No message found"
  }

  companion object {
    fun <T> success(data: T): Lce<T> = Lce(false, null, data)

    fun <T> failure(error: Throwable): Lce<T> = Lce(false, error)

    fun <T> loading(): Lce<T> = Lce(true)
  }
}