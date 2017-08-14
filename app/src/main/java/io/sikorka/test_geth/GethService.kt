package io.sikorka.test_geth

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.sikorka.test_geth.node.configuration.ConfigurationFactory
import io.sikorka.test_geth.node.configuration.Network
import org.ethereum.geth.*
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class GethService : Service() {

  private lateinit var notificationManager: NotificationManager
  private lateinit var ethContext: Context

  @Inject internal lateinit var configurationFactory: ConfigurationFactory

  override fun onBind(intent: Intent): IBinder? = null

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
    try {
      start()
      return START_STICKY
    } catch(e: Exception) {
      stopSelf()
      return START_NOT_STICKY
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

    val notification = NotificationCompat.Builder(this, "sikorka_geth_channel_01")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Geth Test")
        .setContentText(message)
        .setOngoing(true)
        .setNumber(count)
        .setContentIntent(pendingIntent).build()
    return notification
  }

  fun start() {
    val configuration = configurationFactory.configuration(Network.ROPSTEN)
    val dataDir = configuration.dataDir
    val nodeConfig = configuration.nodeConfig

    val node = Geth.newNode(dataDir.absolutePath, nodeConfig)
    node.start()

    schedulerPeerCheck(node)

    //print traffic of the node
    val handler = object : NewHeadHandler {
      override fun onError(error: String) {}
      override fun onNewHead(header: Header) {
        println("#" + header.number + ": " + header.hash.hex.substring(0, 10) + "…\n")
      }
    }

    val ec = node.ethereumClient
    ec.subscribeNewHead(ethContext, handler, 32)
  }

  private fun syncProgress(ec: EthereumClient): String {
    return try {
      val syncProgress = ec.syncProgress(ethContext)
      "block: ${ec.getBlockByNumber(ethContext, -1).number} of ${syncProgress.highestBlock}"
    } catch (ex: Exception) {
      println(ex.message)
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
        println(message)
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
  }
}
