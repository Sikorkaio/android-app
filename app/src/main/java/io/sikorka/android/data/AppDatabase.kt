package io.sikorka.android.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContract
import io.sikorka.android.data.contracts.deployed.DeployedSikorkaContractDao
import io.sikorka.android.data.contracts.pending.PendingContract
import io.sikorka.android.data.contracts.pending.PendingContractDao
import io.sikorka.android.data.transactions.PendingTransaction
import io.sikorka.android.data.transactions.PendingTransactionDao

@Database(
  version = 1,
  entities = [
    PendingContract::class,
    PendingTransaction::class,
    AccountBalance::class,
    DeployedSikorkaContract::class
  ]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun accountBalanceDao(): AccountBalanceDao
  abstract fun deployedSikorkaContractDao(): DeployedSikorkaContractDao
  abstract fun pendingContractDao(): PendingContractDao
  abstract fun pendingTransactionDao(): PendingTransactionDao
}