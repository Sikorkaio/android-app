package io.sikorka.android.mvp

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in Data>(itemView: View) : RecyclerView.ViewHolder(itemView) {

  abstract fun update()
}