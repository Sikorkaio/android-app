package io.sikorka.android.io.detectors

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import io.reactivex.Observable

interface BtScanner {

  fun discover(context: Context): Observable<BluetoothDevice>

  fun btSupport(): Boolean

  fun enableBt(activity: Activity, requestCode: Int)
}