package io.sikorka.android.io.detectors

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class BtConnectorImpl
@Inject
constructor() : BtConnector {

  override fun connect(device: BluetoothDevice): Single<BtService> {
    return attemptConnection(device)
      .subscribeOn(Schedulers.single())
      .observeOn(Schedulers.single())
      .map { BtService(it) }
  }

  private fun attemptConnection(device: BluetoothDevice): Single<BluetoothSocket> {
    return Single.create {
      val btAdapter = BluetoothAdapter.getDefaultAdapter()
      val socket: BluetoothSocket

      try {
        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID))
      } catch (e: IOException) {
        Timber.e(e, "Socket's create() method failed")
        it.onError(e)
        return@create
      }

      btAdapter.cancelDiscovery()

      try {
        // Connect to the remote device through the socket. This call blocks
        // until it succeeds or throws an exception.
        socket.connect()
        Timber.v("Status ${socket.isConnected}")
      } catch (connectException: IOException) {
        // Unable to connect; close the socket and return.
        try {
          Timber.v("closing socket")
          socket.close()
        } catch (closeException: IOException) {
          Timber.e(closeException, "Could not close the client socket")
        }

        it.onError(connectException)
        return@create
      }

      it.onSuccess(socket)
    }
  }

  companion object {
    private const val BT_UUID = "1e0ca4ea-299d-4335-93eb-27fcfe7fa848"
  }
}