package io.sikorka.android.di.modules

import io.sikorka.android.data.AppDatabase
import io.sikorka.android.data.PendingContractDataSource
import io.sikorka.android.di.providers.AppDatabaseProvider
import io.sikorka.android.di.providers.KeystorePathProvider
import io.sikorka.android.di.providers.PendingContractDataSourceProvider
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.RxBusImpl
import io.sikorka.android.io.StorageManager
import io.sikorka.android.io.StorageManagerImpl
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.PassphraseValidatorImpl
import io.sikorka.android.node.configuration.ConfigurationProvider
import io.sikorka.android.node.configuration.ConfigurationProviderImpl
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.settings.AppPreferencesImpl
import io.sikorka.android.ui.settings.DebugPreferencesStore
import io.sikorka.android.ui.settings.DebugPreferencesStoreImpl
import io.sikorka.android.utils.schedulers.SchedulerProvider
import io.sikorka.android.utils.schedulers.SchedulerProviderImpl
import toothpick.config.Module

class SikorkaModule : Module() {
  init {
    bind(String::class.java).withName(KeystorePath::class.java).toProvider(KeystorePathProvider::class.java)
    bind(SchedulerProvider::class.java).to(SchedulerProviderImpl::class.java).singletonInScope()
    bind(PassphraseValidator::class.java).to(PassphraseValidatorImpl::class.java)
    bind(AppPreferences::class.java).to(AppPreferencesImpl::class.java)
    bind(RxBus::class.java).to(RxBusImpl::class.java).singletonInScope()
    bind(AppDatabase::class.java).toProvider(AppDatabaseProvider::class.java).providesSingletonInScope()
    bind(PendingContractDataSource::class.java).toProvider(PendingContractDataSourceProvider::class.java).providesSingletonInScope()
    bind(StorageManager::class.java).to(StorageManagerImpl::class.java).singletonInScope()
    bind(DebugPreferencesStore::class.java).to(DebugPreferencesStoreImpl::class.java).singletonInScope()

    bind(ConfigurationProvider::class.java).to(ConfigurationProviderImpl::class.java).singletonInScope()
  }
}