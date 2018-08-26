package io.sikorka.android.data.contracts.pending

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_contracts")
data class PendingContract(
  @PrimaryKey(autoGenerate = true)
  var id: Int = 0,
  @ColumnInfo(name = "contract_address")
  var contractAddress: String,
  @ColumnInfo(name = "transaction_hash")
  var transactionHash: String,
  @ColumnInfo(name = "date_created")
  var dateCreated: Long
)