package io.sikorka.android.di.modules

import com.squareup.moshi.Moshi
import io.sikorka.android.core.ServiceManager
import io.sikorka.android.core.ServiceManagerImpl
import io.sikorka.android.core.accounts.PassphraseValidator
import io.sikorka.android.core.accounts.PassphraseValidatorImpl
import io.sikorka.android.core.configuration.ConfigurationProvider
import io.sikorka.android.core.configuration.ConfigurationProviderImpl
import io.sikorka.android.core.configuration.peers.PeerDataSource
import io.sikorka.android.core.configuration.peers.PeerDataSourceImpl
import io.sikorka.android.core.ethereumclient.LightClientProvider
import io.sikorka.android.data.AppDatabase
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.data.location.UserLocationProvider
import io.sikorka.android.data.syncstatus.SyncStatusProvider
import io.sikorka.android.data.transactions.PendingTransactionDao
import io.sikorka.android.di.providers.AccountBalanceDaoProvider
import io.sikorka.android.di.providers.AppDatabaseProvider
import io.sikorka.android.di.providers.ApplicationCacheProvider
import io.sikorka.android.di.providers.DeployedSikorkaContractDaoProvider
import io.sikorka.android.di.providers.KeystorePathProvider
import io.sikorka.android.di.providers.MoshiProvider
import io.sikorka.android.di.providers.PendingContractDaoProvider
import io.sikorka.android.di.providers.PendingTransactionDaoProvider
import io.sikorka.android.di.qualifiers.ApplicationCache
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.RxBusImpl
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.StorageManagerImpl
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.settings.AppPreferencesImpl
import io.sikorka.android.ui.settings.DebugPreferencesStore
import io.sikorka.android.ui.settings.DebugPreferencesStoreImpl
import io.sikorka.android.utils.schedulers.SchedulerProvider
import io.sikorka.android.utils.schedulers.SchedulerProviderImpl
import toothpick.config.Module
import java.io.File

class SikorkaModule : Module() {
  init {
    bind(String::class.java).withName(KeystorePath::class.java)
      .toProvider(KeystorePathProvider::class.java)
    bind(SchedulerProvider::class.java).to(SchedulerProviderImpl::class.java).singletonInScope()
    bind(PassphraseValidator::class.java).to(PassphraseValidatorImpl::class.java)
    bind(AppPreferences::class.java).to(AppPreferencesImpl::class.java)
    bind(RxBus::class.java).to(RxBusImpl::class.java).singletonInScope()

    bind(AppDatabase::class.java).toProvider(AppDatabaseProvider::class.java)
      .providesSingletonInScope()

    bind(PendingContractDao::class.java).toProvider(PendingContractDaoProvider::class.java)
      .providesSingletonInScope()
    bind(PendingTransactionDao::class.java).toProvider(PendingTransactionDaoProvider::class.java)
      .providesSingletonInScope()
    bind(AccountBalanceDao::class.java).toProvider(AccountBalanceDaoProvider::class.java)
      .providesSingletonInScope()
    bind(DeployedSikorkaContractDao::class.java)
      .toProvider(DeployedSikorkaContractDaoProvider::class.java)
      .providesSingletonInScope()

    bind(StorageManager::class.java).to(StorageManagerImpl::class.java).singletonInScope()
    bind(DebugPreferencesStore::class.java).to(DebugPreferencesStoreImpl::class.java)
      .singletonInScope()

    bind(ConfigurationProvider::class.java).to(ConfigurationProviderImpl::class.java)
      .singletonInScope()

    bind(Moshi::class.java).toProvider(MoshiProvider::class.java).providesSingletonInScope()

    bind(SyncStatusProvider::class.java).singletonInScope()
    bind(LightClientProvider::class.java).singletonInScope()
    bind(UserLocationProvider::class.java).singletonInScope()

    bind(PeerDataSource::class.java).to(PeerDataSourceImpl::class.java)
    bind(File::class.java).withName(ApplicationCache::class.java)
      .toProvider(ApplicationCacheProvider::class.java)

    bind(ServiceManager::class.java).to(ServiceManagerImpl::class.java)
  }
}