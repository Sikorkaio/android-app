package io.sikorka.android.ui

import android.support.v7.widget.RecyclerView

abstract class DataAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
  private var data: List<T> = emptyList()

  override fun getItemCount(): Int = data.size

  protected fun getItem(position: Int): T = data[position]

  fun setList(data: List<T>) {
    this.data = data
    notifyDataSetChanged()
  }
}