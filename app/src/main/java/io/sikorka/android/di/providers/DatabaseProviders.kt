package io.sikorka.android.di.providers

import android.app.Application
import android.arch.persistence.room.Room
import io.sikorka.android.helpers.AppDatabase
import io.sikorka.android.node.contracts.PendingContractDataSource
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseProvider
@Inject
constructor(private val application: Application) : Provider<AppDatabase> {
  override fun get(): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "sikorka.db").build()
  }
}

class PendingContractDataSourceProvider
@Inject
constructor(private val database: AppDatabase) : Provider<PendingContractDataSource> {
  override fun get(): PendingContractDataSource = database.pendingContractDataSource()
}
