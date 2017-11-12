package io.sikorka.android.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
abstract class PendingTransactionDao : BaseDao<PendingTransaction> {

  @Query("select * from pending_transactions where transaction_status = -1")
  abstract fun pendingTransactions() : LiveData<List<PendingTransaction>>

  @Query("select * from pending_transactions where transaction_status = -1")
  abstract fun pendingTransaction() : Flowable<List<PendingTransaction>>

}