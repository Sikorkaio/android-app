package io.sikorka.android.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration

@Database(
    version = 3,
    entities = arrayOf(
        PendingContract::class,
        PendingTransaction::class
    )
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun pendingContractDao(): PendingContractDao
  abstract fun pendingTransactionDao(): PendingTransactionDao

  companion object {
    val migration_1_2 = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE pending_contracts " +
            "ADD COLUMN date_created INTEGER")
      }
    }

    val migration_2_3 = object : Migration(2, 3) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
          CREATE TABLE pending_transactions (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            transaction_hash TEXT NOT NULL,
            date_added INTEGER NOT NULL,
            transaction_status INTEGER NOT NULL
          )""".trimIndent())
      }
    }
  }
}