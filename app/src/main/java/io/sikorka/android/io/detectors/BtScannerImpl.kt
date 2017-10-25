package io.sikorka.android.io.detectors

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BtScannerImpl
@Inject constructor(private val schedulerProvider: SchedulerProvider) : BtScanner {
  private val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)

  override fun btSupport(): Boolean {
    val btAdapter = BluetoothAdapter.getDefaultAdapter()
    return btAdapter != null
  }

  override fun enableBt(activity: Activity, requestCode: Int) {
    val btAdapter = BluetoothAdapter.getDefaultAdapter()
    val enabled = btAdapter.isEnabled
    Timber.v("Bt adapter was enabled $enabled")
    if (!enabled) {
      val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
      activity.startActivityForResult(enableBtIntent, requestCode)
    }
  }

  override fun discover(context: Context): Observable<BluetoothDevice> = Observable.create<BluetoothDevice> { emitter ->
    val btAdapter = BluetoothAdapter.getDefaultAdapter()
    val receiver = object : BroadcastReceiver() {
      override fun onReceive(ctx: Context?, intent: Intent?) {
        if (ctx == null || intent == null) {
          return
        }
        if (intent.action != BluetoothDevice.ACTION_FOUND) {
          return
        }
        val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        emitter.onNext(device)
      }
    }
    context.registerReceiver(receiver, intentFilter)

    btAdapter.startDiscovery()

    emitter.setCancellable {
      try {
        context.unregisterReceiver(receiver)
      } catch (ignored: Exception) {
      }
      btAdapter.cancelDiscovery()
    }
  }.distinct()
      .subscribeOn(schedulerProvider.io())
      .take(10, TimeUnit.SECONDS)
      .observeOn(schedulerProvider.main())

}