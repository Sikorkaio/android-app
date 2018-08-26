package io.sikorka.android.data.transactions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import io.sikorka.android.data.BaseDao

@Dao
abstract class PendingTransactionDao : BaseDao<PendingTransaction> {

  @Query("select * from pending_transactions where transaction_status = -1")
  abstract fun pendingTransactions(): LiveData<List<PendingTransaction>>

  @Query("select * from pending_transactions where transaction_status = -1")
  abstract fun pendingTransaction(): Flowable<List<PendingTransaction>>
}