package io.sikorka.android

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.sikorka.android.events.RxBus
import io.sikorka.android.node.GethNode
import io.sikorka.android.node.SyncStatus
import io.sikorka.android.node.contracts.DeployedContractMonitor
import io.sikorka.android.node.contracts.PrepareTransactionStatusEvent
import io.sikorka.android.node.contracts.TransactionStatusEvent
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.utils.schedulers.SchedulerProvider
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SikorkaService : Service() {

  private lateinit var notificationManager: NotificationManager

  @Inject lateinit var gethNode: GethNode
  @Inject lateinit var contractMonitor: DeployedContractMonitor
  @Inject lateinit var schedulerProvider: SchedulerProvider
  @Inject lateinit var bus: RxBus

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
          notificationManager.notify(SikorkaService.NOTIFICATION_ID, notification)
        }) {
          Timber.v(it, "failed")
        })

    contractMonitor.setOnDeploymentStatusUpdateListener { status, contractAddress, txHash ->
      val notification = statusNotification(status, contractAddress, txHash)
      notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, notification)
    }
    bus.register(this, PrepareTransactionStatusEvent::class.java, {
      Timber.v("Handling status")
      Completable.timer(4, TimeUnit.SECONDS)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.main())
          .subscribe({
            notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, transactionSuccess())
            bus.post(TransactionStatusEvent(it.txHash, it.success))
          })
    })
  }

  override fun onDestroy() {
    bus.unregister(this)
    stopForeground(true)
    compositeDisposable.clear()
    gethNode.stop()
    contractMonitor.stop()
    Toothpick.closeScope(this)
    super.onDestroy()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.v("Starting Node")
    return try {
      gethNode.start()
      contractMonitor.start()
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

      val channelIdStatus = "sikorka_deployment_update_channel"
      val channelNameStatus = getString(R.string.notification_channel__deployment_status)
      val channelStatus = NotificationChannel(channelIdStatus, channelNameStatus, NotificationManager.IMPORTANCE_HIGH)

      notificationManager.createNotificationChannels(listOf(channel, channelStatus))
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

  private fun statusNotification(status: Boolean, contractAddress: String, txHash: String) : Notification {
    val message = if (status) {
      getString(R.string.contract_deployment__status_success_notification, contractAddress, txHash)
    } else {
      getString(R.string.contract_deployment__status_failed_notification, contractAddress, txHash)
    }

    return NotificationCompat.Builder(this, "sikorka_geth_channel_02")
        .setSmallIcon(R.drawable.ic_stat_ic_launcher)
        .setContentTitle(getString(R.string.notification__sikorka_node_title))
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setGroup("")
        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
        .build()
  }

  private fun transactionSuccess(): Notification {
    val message = "Your transaction has been mined. 100 Sikorka example discount tokens have been transferred to your account"
    return NotificationCompat.Builder(this, "sikorka_deployment_update_channel")
        .setSmallIcon(R.drawable.ic_stat_ic_launcher)
        .setContentTitle("Transaction Mined")
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setGroup("")
        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
        .build()
  }


  companion object {
    const val NOTIFICATION_ID = 1337
    const val STATUS_NOTIFICATION_ID: Int = 1338

    fun stop(context: android.content.Context) {
      context.stopService(Intent(context, SikorkaService::class.java))
    }

    fun start(context: android.content.Context) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(Intent(context, SikorkaService::class.java))
      } else {
        context.startService(Intent(context, SikorkaService::class.java))
      }
    }


  }
}

private fun SyncStatus.empty(): Boolean = peers == 0 && currentBlock == 0L && highestBlock == 0L
