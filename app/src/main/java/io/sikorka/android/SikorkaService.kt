package io.sikorka.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.monitor.AccountBalanceMonitor
import io.sikorka.android.core.monitor.DeployedContractMonitor
import io.sikorka.android.core.monitor.PendingContractMonitor
import io.sikorka.android.core.monitor.PendingTransactionMonitor
import io.sikorka.android.core.monitor.PrepareTransactionStatus
import io.sikorka.android.core.monitor.TransactionStatus
import io.sikorka.android.data.syncstatus.SyncStatus
import io.sikorka.android.events.Event
import io.sikorka.android.events.EventLiveDataProvider
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.utils.schedulers.AppDispatchers
import io.sikorka.android.utils.schedulers.AppSchedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SikorkaService : LifecycleService() {

  private val notificationManager: NotificationManager by inject()

  private val gethNode: GethNode by inject()

  private val contractMonitor: PendingContractMonitor by inject()

  private val pendingTransactionMonitor: PendingTransactionMonitor by inject()

  private val accountBalanceMonitor: AccountBalanceMonitor by inject()

  private val deployedContractMonitor: DeployedContractMonitor by inject()

  private val schedulers: AppSchedulers by inject()

  private val dispatchers: AppDispatchers by inject()

  private val eventProvider: EventLiveDataProvider by inject()

  private val disposables = CompositeDisposable()

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
    val startNotification = notification("Starting...")
    startForeground(NOTIFICATION_ID, startNotification)
  }

  override fun onDestroy() {
    Timber.v("Sikorka service is stopping")
    stop()
    super.onDestroy()
  }

  private fun stop() {
    disposables.clear()

    launch(dispatchers.io) {

      pendingTransactionMonitor.stop()
      accountBalanceMonitor.stop()
      deployedContractMonitor.stop()
      contractMonitor.stop()
      disposables.clear()
      gethNode.stop()
    }

    stopForeground(true)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
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
      .subscribeOn(schedulers.io)
      .observeOn(schedulers.io)
      .subscribe({
        val notification = createNotification(it)
        notificationManager.notify(NOTIFICATION_ID, notification)
      }) {
        Timber.v(it, "failed")
      }

    contractMonitor.setStatusUpdateListener { receipt ->
      receipt.run {
        val notification = statusNotification(successful, contractAddress(), txHash)
        notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, notification)
      }
    }

    eventProvider.observe(this) {
      val content = it.getContentIfNotHandled() ?: return@observe

      if (content is PrepareTransactionStatus) {
        launch(UI) {
          delay(10_000)
          Timber.v("Handling status")
          notificationManager.notify(SikorkaService.STATUS_NOTIFICATION_ID, transactionSuccess())
          eventProvider.post(Event(TransactionStatus(content.txHash, content.success)))
        }
      }
    }
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
      resources.getQuantityString(
        R.plurals.notification__network_statistics,
        status.peers,
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
    val message = "Your transaction has been mined. " +
      "100 Sikorka example discount tokens have been transferred to your account"
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