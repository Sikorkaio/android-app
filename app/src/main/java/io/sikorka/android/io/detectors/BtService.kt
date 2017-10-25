package io.sikorka.android.io.detectors


import android.bluetooth.BluetoothSocket
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.sikorka.android.BuildConfig
import org.ethereum.geth.Address
import org.ethereum.geth.Geth
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class BtService(private val socket: BluetoothSocket) {
  private val observable: Observable<String>

  init {
    observable = Observable.using({
      return@using socket
    }, {
      responses(it)
    }, {
      it.cleanup()
    })
  }


  private fun responses(socket: BluetoothSocket): Observable<out String> {
    return try {
      val input = InputStreamReader(socket.inputStream)
      val bufferedReader = BufferedReader(input)
      Observable.create(ProtocolResponseReader(bufferedReader))
    } catch (ex: IOException) {
      Observable.error<String>(ex)
    }
  }

  private fun BluetoothSocket.cleanup() {
    Timber.v("Cleaning bluetooth socket")
    if (this.isConnected) {
      try {
        this.close()
      } catch (ex: IOException) {
        Timber.d(ex, "Failed to clause the bluetooth socket")
      }
    }
  }

  fun getDetectorEthAddress(): Single<Address> {
    return observable.subscribeOn(Schedulers.io()).doOnSubscribe {
      Timber.v("Requesting ETH ADDRESS")
      write(ADDRESS_REQUEST)
    }.flatMap { addressHex ->
      if (addressHex.contains(ADDRESS_REQUEST)) {
        Observable.create<Address> {
          val value = addressHex.substringAfter("::")
          try {
            val address = Geth.newAddressFromHex(value)
            it.onNext(address)
            it.onComplete()
          } catch (e: Exception) {
            it.onError(IllegalArgumentException("Address {$value}", e))
          }
        }
      } else {
        Observable.error<Address>(IllegalArgumentException("Expected $ADDRESS_REQUEST"))
      }
    }.firstOrError()
  }

  private fun write(text: String) {
    val message = (text + "\r\n").toByteArray()
    val outputStream = socket.outputStream
    outputStream.write(message)
  }

  private class ProtocolResponseReader(private val bufferedReader: BufferedReader) : ObservableOnSubscribe<String> {
    override fun subscribe(emitter: ObservableEmitter<String>) {
      try {
        while (true) {
          // Read from the InputStream.
          val line = bufferedReader.readLine()
          if (line.contains("END")) {
            emitter.onComplete()
            break
          }

          if (BuildConfig.DEBUG) {
            Timber.v("received => $line")
          }

          emitter.onNext(line)
        }
      } catch (e: IOException) {
        Timber.d(e, "Input stream was disconnected")
        if (!emitter.isDisposed) {
          emitter.onError(e)
        }
      }
    }
  }


  companion object {
    private const val ADDRESS_REQUEST = "ETH_ADDRESS"
  }

}