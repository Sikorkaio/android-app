package io.sikorka.android.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration

@Database(
    version = 2,
    entities = arrayOf(
        PendingContract::class
    )
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun pendingContractDataSource(): PendingContractDataSource

  companion object {
    val migration_1_2 = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE pending_contracts " +
            "ADD COLUMN date_created INTEGER")
      }

    }
  }
}