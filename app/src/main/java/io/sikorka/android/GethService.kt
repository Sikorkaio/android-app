package io.sikorka.android

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.configuration.ConfigurationFactory
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.main.MainActivity
import org.ethereum.geth.Context
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.Node
import timber.log.Timber
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class GethService : Service() {

  private lateinit var notificationManager: NotificationManager
  private lateinit var ethContext: Context

  @Inject internal lateinit var configurationFactory: ConfigurationFactory
  @Inject internal lateinit var appPreferences: AppPreferences

  override fun onBind(intent: Intent): IBinder? = null

  private var node: Node? = null

  override fun onCreate() {
    val scope = Toothpick.openScopes(application, this)
    Toothpick.inject(this, scope)
    super.onCreate()
    ethContext = Context()
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
      start()
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
        .setContentIntent(pendingIntent).build()
  }

  fun start() {
    if (node != null) {
      return
    }

    val configuration = configurationFactory.configuration(appPreferences.selectedNetwork())
    configuration.prepare()
    val dataDir = configuration.dataDir
    val nodeConfig = configuration.nodeConfig

    Timber.v("node data directory will be in $dataDir")
    node = Geth.newNode(dataDir.absolutePath, nodeConfig)
    val node = node ?: fail("what node?")
    node.start()
    schedulerPeerCheck(node)

  }

  private fun syncProgress(ec: EthereumClient): String {
    return try {
      val syncProgress = ec.syncProgress(ethContext)
      "block: ${ec.getBlockByNumber(ethContext, -1).number} of ${syncProgress.highestBlock}"
    } catch (ex: Exception) {
      Timber.v(ex)
      ""
    }
  }

  private fun schedulerPeerCheck(node: Node) {
    Timer().scheduleAtFixedRate(object : TimerTask() {
      override fun run() {

        val peerInfos = node.peersInfo
        var message = "Connected peers: ${peerInfos.size()}"
        val syncProgress = syncProgress(node.ethereumClient)
        if (syncProgress.isNotBlank()) {
          message = "$message - $syncProgress"
        }
        Timber.v("checking sync progress $message")
        val notification = createNotification(message, peerInfos.size().toInt())
        notificationManager.notify(NOTIFICATION_ID, notification)
      }

    }, 0, 30000)//put here time 1000 milliseconds=1 second
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
