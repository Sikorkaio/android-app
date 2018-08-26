package io.sikorka.android.data.contracts.pending

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import io.sikorka.android.data.BaseDao

@Dao
abstract class PendingContractDao : BaseDao<PendingContract> {

  @Query("SELECT * FROM pending_contracts")
  abstract fun getAllPendingContracts(): Flowable<List<PendingContract>>

  @Query("DELETE from pending_contracts where contract_address = :addressHex")
  abstract fun deleteByContractAddress(addressHex: String)
}