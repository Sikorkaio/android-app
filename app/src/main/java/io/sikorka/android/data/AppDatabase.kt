package io.sikorka.android.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.sikorka.android.data.balance.AccountBalance
import io.sikorka.android.data.balance.AccountBalanceDao

@Database(
    version = 1,
    entities = arrayOf(
        PendingContract::class,
        PendingTransaction::class,
        AccountBalance::class
    )
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun pendingContractDao(): PendingContractDao
  abstract fun pendingTransactionDao(): PendingTransactionDao
  abstract fun accountBalanceDao(): AccountBalanceDao
}