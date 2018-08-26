package io.sikorka.android.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<in T> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg obj: T)

  @Delete
  fun delete(obj: T)
}