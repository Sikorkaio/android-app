package io.sikorka.android

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.monitor.*
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.events.RxBus
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.utils.schedulers.SchedulerProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

class SikorkaService : Service() {

  @Inject
  lateinit var notificationManager: NotificationManager
  @Inject
  lateinit var gethNode: GethNode
  @Inject
  lateinit var contractMonitor: PendingContractMonitor
  @Inject
  lateinit var pendingTransactionMonitor: PendingTransactionMonitor
  @Inject
  lateinit var accountBalanceMonitor: AccountBalanceMonitor
  @Inject
  lateinit var deployedContractMonitor: DeployedContractMonitor
  @Inject
  lateinit var schedulerProvider: SchedulerProvider
  @Inject
  lateinit var bus: RxBus

  override fun onBind(intent: Intent): IBinder? = null

  private lateinit var scope: Scope

  private val disposables = CompositeDisposable()

  override fun onCreate() {
    scope = Toothpick.openScopes(application, this)
    Toothpick.inject(this, scope)
    super.onCreate()
    createNotificationChannel()
    val startNotification = notification("Starting...")
    startForeground(NOTIFICATION_ID, startNotification)
  }

  override fun onDestroy() {
    Timber.v("Sikorka service is stopping")
    stop()
    Toothpick.closeScope(this)
    super.onDestroy()
  }

  private fun stop() {
    async(CommonPool) {
      contractMonitor.stop()
      pendingTransactionMonitor.stop()
      accountBalanceMonitor.stop()
      deployedContractMonitor.stop()
      bus.unregister(this)
      disposables.clear()
      gethNode.stop()
    }

    stopForeground(true)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.v("Sikorka service is starting")
    return try {
      start()
      START_STICKY
    } catch (e: Exception) {
      stopSelf()
      START_NOT_STICKY
    }
  }

  private fun start() {
    gethNode.start()

    contractMonitor.start()
    pendingTransactionMonitor.start()
    accountBalanceMonitor.start()
    deployedContractMonitor.start()

    disposables += gethNode.status()
      .distinct()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.io())
      .subscribe({
        val notification = createNotification(it)
        notificationManager.notify(NOTIFICATION_ID, notification)
      }) {
        Timber.v(it, "failed")
      }

    contractMonitor.setOnDeploymentStatusUpdateListener { receipt ->
      receipt.run {
        val notification = statusNotification(successful, contractAddress(), txHash)
        notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, notification)
      }
    }

    bus.register(this, PrepareTransactionStatusEvent::class.java, {
      async(UI) {
        delay(10_000)
        Timber.v("Handling status")
        notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, transactionSuccess())
        bus.post(TransactionStatusEvent(it.txHash, it.success))
      }
    })
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    super.onTaskRemoved(rootIntent)
    Timber.v("Task is being removed, stopping service")
    stopSelf()
    notificationManager.cancel(NOTIFICATION_ID)
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return
    }

    val channelId = "sikorka_geth_channel_01"
    val channelName = "service"
    val channel = NotificationChannel(
      channelId,
      channelName,
      NotificationManager.IMPORTANCE_DEFAULT
    )

    val channelIdStatus = "sikorka_deployment_update_channel"
    val channelNameStatus = getString(R.string.notification_channel__deployment_status)
    val channelStatus = NotificationChannel(
      channelIdStatus,
      channelNameStatus,
      NotificationManager.IMPORTANCE_HIGH
    )

    notificationManager.createNotificationChannels(listOf(channel, channelStatus))
  }

  private fun createNotification(status: SyncStatus): Notification {
    val message = if (status.empty()) {
      getString(R.string.notification__not_syncing)
    } else {
      getString(
        R.string.notification__network_statistics,
        status.peers,
        status.currentBlock,
        status.highestBlock
      )
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

  private fun statusNotification(
    status: Boolean,
    contractAddress: String,
    txHash: String
  ): Notification {
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
    val message =
      "Your transaction has been mined. 100 Sikorka example discount tokens have been transferred to your account"
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
