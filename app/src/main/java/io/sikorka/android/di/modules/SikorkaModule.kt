package io.sikorka.android.di.modules

import android.app.NotificationManager
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.sikorka.android.core.GethNode
import io.sikorka.android.core.ServiceManager
import io.sikorka.android.core.ServiceManagerImpl
import io.sikorka.android.core.accounts.AccountRepository
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.PassphraseValidatorImpl
import io.sikorka.android.core.configuration.ConfigurationFactory
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.ConfigurationProviderImpl
import io.sikorka.android.core.configuration.PeerHelper
import io.sikorka.android.core.configuration.RopstenConfiguration
import io.sikorka.android.core.configuration.peers.PeerAdapter
import io.sikorka.android.core.configuration.peers.PeerDataSource
import io.sikorka.android.core.configuration.peers.PeerDataSourceImpl
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.core.monitor.AccountBalanceMonitor
import io.sikorka.android.core.monitor.DeployedContractMonitor
import io.sikorka.android.core.monitor.PendingContractMonitor
import io.sikorka.android.core.monitor.PendingTransactionMonitor
import io.sikorka.android.data.AppDatabase
import io.sikorka.android.data.contracts.ContractRepository
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.RxBusImpl
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.StorageManagerImpl
import io.sikorka.android.io.detectors.BtConnector
import io.sikorka.android.io.detectors.BtConnectorImpl
import io.sikorka.android.io.detectors.BtScanner
import io.sikorka.android.io.detectors.BtScannerImpl
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.settings.AppPreferencesImpl
import io.sikorka.android.ui.settings.DebugPreferencesStore
import io.sikorka.android.ui.settings.DebugPreferencesStoreImpl
import io.sikorka.android.utils.schedulers.AppDispatchers
import io.sikorka.android.utils.schedulers.AppSchedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.rx2.asCoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import java.util.concurrent.Executors

object Names {
  const val KEYSTORE_PATH = "keystore_path"
  const val APPLICATION_CACHE = "application_cache"
}

val sikorkaModule = module {

  var count = 0
  val executor = Executors.newSingleThreadExecutor { Thread(it, "db-operations") }
  val monitorExecutor = Executors.newSingleThreadExecutor { Thread(it, "monitor") }
  val computation = Executors.newFixedThreadPool(2) {
    Thread(it, "computation-${++count}")
  }

  single {
    Moshi.Builder()
      .add(PeerAdapter())
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  single(Names.KEYSTORE_PATH) { "${androidContext().filesDir}/keystore" }
  single(Names.APPLICATION_CACHE) { androidContext().externalCacheDir ?: androidContext().cacheDir }

  single {
    AppSchedulers(
      io = Schedulers.io(),
      computation = Schedulers.from(computation),
      main = AndroidSchedulers.mainThread(),
      db = Schedulers.from(executor),
      monitor = Schedulers.from(monitorExecutor)
    )
  }

  single {
    val appSchedulers = get<AppSchedulers>()

    AppDispatchers(
      io = appSchedulers.io.asCoroutineDispatcher(),
      computation = appSchedulers.computation.asCoroutineDispatcher(),
      main = UI,
      db = appSchedulers.db.asCoroutineDispatcher(),
      monitor = appSchedulers.monitor.asCoroutineDispatcher()
    )
  }

  factory { PassphraseValidatorImpl() as PassphraseValidator }

  factory { AppPreferencesImpl(get()) as AppPreferences }
  single { RxBusImpl() as RxBus }

  single { create<GethNode>() }

  single {
    Room.databaseBuilder(androidContext(), AppDatabase::class.java, "sikorka.db")
      .build()
  }
  single { get<AppDatabase>().pendingContractDao() }
  single { get<AppDatabase>().pendingTransactionDao() }
  single { get<AppDatabase>().accountBalanceDao() }
  single { get<AppDatabase>().deployedSikorkaContractDao() }

  single { StorageManagerImpl(get(), get()) as StorageManager }
  single { DebugPreferencesStoreImpl(get()) as DebugPreferencesStore }
  single { ConfigurationProviderImpl(get(), get()) as ConfigurationProvider }
  single { create<RopstenConfiguration>() }
  factory { create<PeerHelper>() }

  factory { ConfigurationFactory() }

  single { create<PendingContractMonitor>() }
  single { create<PendingTransactionMonitor>() }
  single { create<AccountBalanceMonitor>() }
  single { create<DeployedContractMonitor>() }

  single { SyncStatusProvider() }
  single { LightClientProvider() }
  single { UserLocationProvider() }

  factory { PeerDataSourceImpl(get(), get(), get(Names.APPLICATION_CACHE)) as PeerDataSource }
  factory { create<ServiceManagerImpl>() as ServiceManager }

  factory { AccountRepository(get(Names.KEYSTORE_PATH), get(), get()) }
  factory { ContractRepository(get(), get(), get(), get()) }

  factory { BtScannerImpl(get()) as BtScanner }
  factory { BtConnectorImpl() as BtConnector }

  single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
  single { checkNotNull(androidContext().getSystemService<NotificationManager>()) }
  single { androidContext().assets }
}