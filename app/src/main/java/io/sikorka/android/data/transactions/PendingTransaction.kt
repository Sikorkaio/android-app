package io.sikorka.android.data.transactions

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.IntDef

@Entity(tableName = "pending_transactions")
data class PendingTransaction(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "transaction_hash")
    var txHash: String,
    @ColumnInfo(name = "date_added")
    var dateAdded: Long,
    @TransactionStatus.Status
    @ColumnInfo(name = "transaction_status")
    var status: Int = TransactionStatus.PENDING
)

object TransactionStatus {
  const val PENDING = -1
  const val FAILED = 0
  const val SUCCESS = 1

  @IntDef(
      PENDING.toLong(),
      FAILED.toLong(),
      SUCCESS.toLong()
  )
  annotation class Status
}