package io.sikorka.android.di.providers

import android.app.Application
import android.arch.persistence.room.Room
import io.sikorka.android.data.AppDatabase
import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.data.transactions.PendingTransactionDao
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseProvider
@Inject
constructor(private val application: Application) : Provider<AppDatabase> {
  override fun get(): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "sikorka.db")
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

class AccountBalanceDaoProvider
@Inject
constructor(private val database: AppDatabase) : Provider<AccountBalanceDao> {
  override fun get(): AccountBalanceDao = database.accountBalanceDao()
}

class DeployedSikorkaContractDaoProvider
@Inject
constructor(private val database: AppDatabase) : Provider<DeployedSikorkaContractDao> {
  override fun get(): DeployedSikorkaContractDao = database.deployedSikorkaContractDao()
}