package io.sikorka.android.data.contracts.deployed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deployed_contracts",
    indices = arrayOf(Index(value = "address_hex", unique = true))
)
data class DeployedSikorkaContract(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0,
  @ColumnInfo(name = "name")
  var name: String,
  @ColumnInfo(name = "address_hex")
  var addressHex: String,
  @ColumnInfo(name = "latitude")
  var latitude: Double,
  @ColumnInfo(name = "longitude")
  var longitude: Double
)