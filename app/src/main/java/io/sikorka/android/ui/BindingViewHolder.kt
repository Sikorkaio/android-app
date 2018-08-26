package io.sikorka.android.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BindingViewHolder<in T>(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
  abstract fun bindTo(item: T)
}