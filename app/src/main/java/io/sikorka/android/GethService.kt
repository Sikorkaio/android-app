package io.sikorka.android

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.reactivex.disposables.CompositeDisposable
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.SyncStatus
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

class GethService : Service() {

  private lateinit var notificationManager: NotificationManager

  @Inject lateinit var gethNode: GethNode
  @Inject lateinit var schedulerProvider: SchedulerProvider

  override fun onBind(intent: Intent): IBinder? = null

  private lateinit var scope: Scope
  private val compositeDisposable = CompositeDisposable()

  override fun onCreate() {
    scope = Toothpick.openScopes(application, this)
    Toothpick.inject(this, scope)
    super.onCreate()
    notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
    createNotificationChannel()
    val startNotification = notification("Starting...")
    startForeground(NOTIFICATION_ID, startNotification)

    compositeDisposable.add(gethNode.status()
        .distinct()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.io())
        .subscribe({
          val notification = createNotification(it)
          notificationManager.notify(GethService.NOTIFICATION_ID, notification)
        }) {
          Timber.v(it, "failed")
        })
  }

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.clear()
    stopForeground(true)
    gethNode.stop()
    Toothpick.closeScope(this)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.v("Starting Node")
    return try {
      gethNode.start()
      START_STICKY
    } catch (e: Exception) {
      stopSelf()
      START_NOT_STICKY
    }
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channelId = "sikorka_geth_channel_01"
      val channelName = "service"
      val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
      notificationManager.createNotificationChannel(channel)
    }
  }

  private fun createNotification(status: SyncStatus): Notification {
    val message = if (status.empty()) {
      getString(R.string.notification__not_syncing)
    } else {
      getString(R.string.notification__network_statistics, status.peers, status.currentBlock, status.highestBlock)
    }
    val number = status.peers

    return notification(message, number)
  }

  private fun notification(message: String, number: Int = 0): Notification {
    val notificationIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

    return NotificationCompat.Builder(this, "sikorka_geth_channel_01")
        .setSmallIcon(R.drawable.ic_stat_ic_launcher)
        .setContentTitle(getString(R.string.notification__sikorka_node_title))
        .setContentText(message)
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setNumber(number)
        .setGroup("")
        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
        .setContentIntent(pendingIntent).build()
  }


  companion object {
    const val NOTIFICATION_ID = 1337
    fun stop(context: android.content.Context) {
      context.stopService(Intent(context, GethService::class.java))
    }

    fun start(context: android.content.Context) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(Intent(context, GethService::class.java))
      } else {
        context.startService(Intent(context, GethService::class.java))
      }
    }
  }
}

private fun SyncStatus.empty(): Boolean = peers == 0 && currentBlock == 0L && highestBlock == 0L
