package io.sikorka.android.io.detectors


import android.bluetooth.BluetoothSocket
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class BtService(socket: BluetoothSocket) {
  private val thread: ConnectedThread

  init {
    thread = ConnectedThread(socket)
    thread.start()
  }

  fun write(bytes: ByteArray) {
    thread.write(bytes)
  }

  fun close() {
    thread.cancel()
  }

  private inner class ConnectedThread(private val btSocket: BluetoothSocket) : Thread() {
    private val inputStream: InputStream?
    private val outputStream: OutputStream?
    private lateinit var buffer: ByteArray

    init {
      Timber.v("isConnected ${btSocket.isConnected}")
      var tmpIn: InputStream? = null
      var tmpOut: OutputStream? = null

      // Get the input and output streams; using temp objects because
      // member streams are final.
      try {
        tmpIn = btSocket.inputStream
      } catch (e: IOException) {
        Timber.e(e, "Error occurred when creating input stream")
      }

      try {
        tmpOut = btSocket.outputStream
      } catch (e: IOException) {
        Timber.e(e, "Error occurred when creating output stream")
      }

      inputStream = tmpIn
      outputStream = tmpOut
    }

    override fun run() {
      buffer = ByteArray(1024)
      var numBytes: Int // bytes returned from read()

      // Keep listening to the InputStream until an exception occurs.
      while (true) {
        try {
          // Read from the InputStream.
          numBytes = inputStream!!.read(buffer)
          Timber.v("Read $numBytes from bt socket")
        } catch (e: IOException) {
          Timber.d(e, "Input stream was disconnected")
          break
        }

      }
    }

    // Call this from the main activity to send data to the remote device.
    fun write(bytes: ByteArray) {
      try {
        outputStream!!.write(bytes)
      } catch (e: IOException) {
        Timber.e(e, "Error occurred when sending data")
      }
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
      try {
        btSocket.close()
      } catch (e: IOException) {
        Timber.e(e, "Could not close the connect socket")
      }
    }
  }

}