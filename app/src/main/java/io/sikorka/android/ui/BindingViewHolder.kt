package io.sikorka.android.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindingViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
  abstract fun bindTo(item: T)
}