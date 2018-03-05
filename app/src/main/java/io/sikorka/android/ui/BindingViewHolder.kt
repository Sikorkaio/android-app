package io.sikorka.android.ui

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BindingViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
  abstract fun bindTo(item: T)
}