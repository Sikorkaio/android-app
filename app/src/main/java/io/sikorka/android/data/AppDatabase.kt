package io.sikorka.android.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(
    version = 1,
    entities = arrayOf(
        PendingContract::class
    )
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun pendingContractDataSource(): PendingContractDataSource
}