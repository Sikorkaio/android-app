package io.sikorka.android.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface PendingContractDataSource {

  @Query("SELECT * FROM pending_contracts")
  fun getAllPendingContracts(): List<PendingContract>

  @Insert
  fun insert(pendingContract: PendingContract)

  @Delete
  fun delete(pendingContract: PendingContract)

}