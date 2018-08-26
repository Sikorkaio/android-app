package io.sikorka.android.data.contracts.deployed

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.sikorka.android.data.BaseDao

@Dao
abstract class DeployedSikorkaContractDao : BaseDao<DeployedSikorkaContract> {

  @Query("select * from deployed_contracts")
  abstract fun getDeployedContracts(): LiveData<List<DeployedSikorkaContract>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertAll(items: List<DeployedSikorkaContract>)
}