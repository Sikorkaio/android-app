package io.sikorka.android.io.detectors

import android.bluetooth.BluetoothDevice
import io.reactivex.Single

interface BtConnector {
  fun connect(device: BluetoothDevice): Single<BtService>
}