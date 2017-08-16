package io.sikorka.android.mvp

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<in Data>(itemView: View) : RecyclerView.ViewHolder(itemView) {

  abstract fun update()
}