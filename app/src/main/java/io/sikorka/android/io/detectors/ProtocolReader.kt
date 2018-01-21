package io.sikorka.android.io.detectors

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import timber.log.Timber
import java.io.BufferedReader

class ProtocolReader(private val reader: BufferedReader): ObservableOnSubscribe<String>{
  override fun subscribe(e: ObservableEmitter<String>) {
    try {
      while (true) {
        val line = reader.readLine()
        if (line == "END") {
          e.onComplete()
        } else {
          e.onNext(line)
        }
      }
    } catch (ex : Exception) {
      if (!e.isDisposed) {
        e.onError(ex)
      }
      Timber.e(ex, "Couldn't read socket")
    }
  }

}