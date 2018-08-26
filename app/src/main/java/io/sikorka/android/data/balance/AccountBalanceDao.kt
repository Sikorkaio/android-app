package io.sikorka.android.data.balance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.sikorka.android.data.BaseDao

@Dao
abstract class AccountBalanceDao : BaseDao<AccountBalance> {

  @Query("select * from account_balance where address_hex = :addressHex")
  abstract fun observeBalance(addressHex: String): LiveData<AccountBalance>

  @Query("select * from account_balance where address_hex = :addressHex")
  abstract fun getBalance(addressHex: String): AccountBalance?

  @Query("delete from account_balance where address_hex = :hex")
  abstract fun deleteByHex(hex: String)
}