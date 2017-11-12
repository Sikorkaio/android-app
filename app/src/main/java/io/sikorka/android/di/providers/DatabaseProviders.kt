package io.sikorka.android.di.providers

import android.app.Application
import android.arch.persistence.room.Room
import io.sikorka.android.data.AppDatabase
import io.sikorka.android.data.PendingContractDao
import io.sikorka.android.data.PendingTransactionDao
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseProvider
@Inject
constructor(private val application: Application) : Provider<AppDatabase> {
  override fun get(): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "sikorka.db")
        .addMigrations(AppDatabase.migration_1_2, AppDatabase.migration_2_3)
        .build()
  }
}

class PendingContractDaoProvider
@Inject
constructor(private val database: AppDatabase) : Provider<PendingContractDao> {
  override fun get(): PendingContractDao = database.pendingContractDao()
}

class PendingTransactionDaoProvider
@Inject
constructor(private val database: AppDatabase) : Provider<PendingTransactionDao> {
  override fun get(): PendingTransactionDao = database.pendingTransactionDao()
}
