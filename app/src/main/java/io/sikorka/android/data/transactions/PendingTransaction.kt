package io.sikorka.android.data.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.IntDef

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
    PENDING,
    FAILED,
    SUCCESS
  )
  annotation class Status
}