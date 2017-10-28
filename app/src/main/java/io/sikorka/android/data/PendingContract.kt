package io.sikorka.android.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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