package io.sikorka.android.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

interface BaseDao<in T> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg obj: T)

  @Delete
  fun delete(obj: T)
}