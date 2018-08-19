package io.sikorka.android.data.balance

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

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