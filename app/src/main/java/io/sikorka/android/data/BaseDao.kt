package io.sikorka.android.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert

interface BaseDao<in T> {

  @Insert
  fun insert(vararg obj: T)

  @Delete
  fun delete(obj: T)
}