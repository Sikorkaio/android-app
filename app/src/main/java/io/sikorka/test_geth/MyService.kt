package io.sikorka.test_geth

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import org.ethereum.geth.*
import java.io.File
import java.util.*

class MyService : Service() {

  private lateinit var notificationManager: NotificationManager
  private lateinit var ethContext: Context

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    ethContext = Context()
    notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = createNotification("Starting...")
    startForeground(NOTIFICATION_ID, notification)
    start()
  }

  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
  }

  private fun createNotification(message: String, count: Int = 0): Notification? {
    val notificationIntent = Intent(this, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

    val notification = NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Geth Test")
        .setContentText(message)
        .setOngoing(true)
        .setNumber(count)
        .setContentIntent(pendingIntent).build()
    return notification
  }


  private fun canWriteToExternalStorage(): Boolean {
    val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    return permission == PackageManager.PERMISSION_GRANTED
  }

  fun start() {
    val dataDir = prepareDataDir()
    val nodeConfig = createConfig()

    val node = Geth.newNode(dataDir.absolutePath, nodeConfig)
    node.start()
    node.stop()
    copyPeers("${dataDir.absolutePath}/GethDroid/static-nodes.json")
    node.start()

    schedulerPeerCheck(node)


    val info = node.nodeInfo
    //create ethereum client

    println("My name: " + info.name + "\n")
    println("My address: " + info.listenerAddress + "\n")
    println("My enode:" + info.enode + "\n")
    println("My protocols: " + info.protocols + "\n\n")


    //print traffic of the node
    val handler = object : NewHeadHandler {
      override fun onError(error: String) {}
      override fun onNewHead(header: Header) {
        println("#" + header.number + ": " + header.hash.hex.substring(0, 10) + "…\n")
      }
    }

    val ec = node.ethereumClient
    ec.subscribeNewHead(ethContext, handler, 16)
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

  private fun prepareDataDir(): File {
    val internalDataDirPath = "$filesDir/.ethereum"
    val internalDataDir = File(internalDataDirPath)
    val internalDataDirExists = internalDataDir.exists()

    if (!internalDataDirExists) {
      internalDataDir.mkdir()
    }

    var dataDir = internalDataDir

    val internalFiles = internalDataDir.listFiles()

    if (canWriteToExternalStorage()) {
      val externalDataDir = File(Environment.getExternalStorageDirectory(), "/.ethereum")
      if (!externalDataDir.exists()) {
        externalDataDir.mkdir()
        if (internalFiles.isNotEmpty()) {
          internalDataDir.copyToDirectory(externalDataDir)
        }
      }
      val externalFiles = externalDataDir.listFiles()
      if (externalFiles.isNotEmpty()) {
        dataDir = externalDataDir
      }
    }
    return dataDir
  }

  private fun createBootstrap(): Enodes {
    val enode = Geth.newEnode("enode://20c9ad97c081d63397d7b685a412227a40e23c8bdc6688c6f37e97cfbc22d2b4d1db1510d8f61e6a8866ad7f0e17c02b14182d37ea7c3c8b9c2683aeb6b733a1@52.169.14.227:30303")
    val enode2 = Geth.newEnode("enode://6ce05930c72abc632c58e2e4324f7c7ea478cec0ed4fa2528982cf34483094e9cbc9216e7aa349691242576d552a2a56aaeae426c5303ded677ce455ba1acd9d@13.84.180.240:30303")
    val enodes = Geth.newEnodes(2)
    enodes.set(0, enode)
    enodes.set(1, enode2)
    return enodes
  }

  private fun createConfig(): NodeConfig? {
    val nodeConfig = Geth.newNodeConfig()
    nodeConfig.ethereumEnabled = true
    nodeConfig.whisperEnabled = true
    nodeConfig.ethereumGenesis = Geth.testnetGenesis()
    nodeConfig.ethereumNetworkID = 3
    nodeConfig.maxPeers = 30
    nodeConfig.ethereumDatabaseCache = 32
    nodeConfig.bootstrapNodes = createBootstrap()
    return nodeConfig
  }

  private fun copyPeers(peerFile: String) {
    val file = File(peerFile)

    val inputStream = assets.open("peers.json")
    if (file.exists()) {
      file.delete()
      file.createNewFile()
    }
    inputStream.copyToFile(file)
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
  }
}
