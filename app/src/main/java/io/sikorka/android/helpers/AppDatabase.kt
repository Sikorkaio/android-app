package io.sikorka.android.helpers

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.sikorka.android.node.contracts.PendingContract
import io.sikorka.android.node.contracts.PendingContractDataSource

@Database(
    version = 1,
    entities = arrayOf(
        PendingContract::class
    )
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun pendingContractDataSource(): PendingContractDataSource
}