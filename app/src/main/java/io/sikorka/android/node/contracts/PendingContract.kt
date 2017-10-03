package io.sikorka.android.node.contracts

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "pending_contracts")
data class PendingContract(
    @ColumnInfo(name = "contract_address")
    var contractAddress: String,
    @ColumnInfo(name = "transaction_hash")
    var transactionHash: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = -1
)