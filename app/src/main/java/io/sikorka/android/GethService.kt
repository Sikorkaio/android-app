package io.sikorka.android

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.sikorka.android.node.GethNode
import io.sikorka.android.ui.main.MainActivity
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

class GethService : Service() {

  private lateinit var notificationManager: NotificationManager

  @Inject lateinit var gethNode: GethNode

  override fun onBind(intent: Intent): IBinder? = null

  private lateinit var scope: Scope

  override fun onCreate() {
    scope = Toothpick.openScopes(application, this)
    Toothpick.inject(this, scope)
    super.onCreate()
    notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
    createNotificationChannel()
    val notification = createNotification("Starting...")
    startForeground(NOTIFICATION_ID, notification)
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
    stopForeground(true)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.v("Starting Node")
    return try {
      gethNode.setListener { message, peers ->
        val notification = createNotification(message, peers)
        notificationManager.notify(GethService.NOTIFICATION_ID, notification)
      }
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
      val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
      notificationManager.createNotificationChannel(channel)
    }
  }

  private fun createNotification(message: String, count: Int = 0): Notification? {
    val notificationIntent = Intent(this, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

    return NotificationCompat.Builder(this, "sikorka_geth_channel_01")
        .setSmallIcon(R.drawable.ic_stat_ic_launcher)
        .setContentTitle(getString(R.string.notification__sikorka_node_title))
        .setContentText(message)
        .setOngoing(true)
        .setNumber(count)
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
