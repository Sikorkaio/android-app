package io.sikorka.android.data.contracts.deployed

import android.arch.paging.LivePagedListProvider
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.sikorka.android.data.BaseDao

@Dao
abstract class DeployedSikorkaContractDao : BaseDao<DeployedSikorkaContract> {

  @Query("select * from deployed_contracts")
  abstract fun getDeployedContracts(): LivePagedListProvider<Int, DeployedSikorkaContract>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertAll(items: List<DeployedSikorkaContract>)
}