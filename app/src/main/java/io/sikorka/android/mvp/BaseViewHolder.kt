package io.sikorka.android.mvp

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<in Data>(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

  abstract fun update()
}