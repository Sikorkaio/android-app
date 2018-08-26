package io.sikorka.android.data.balance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "account_balance",
  indices = [Index(value = ["address_hex"], unique = true)]
)
data class AccountBalance(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0,
  @ColumnInfo(name = "address_hex")
  var addressHex: String,
  @ColumnInfo(name = "balance")
  var balance: Double
)