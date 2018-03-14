package io.sikorka.android.ui.dialogs.fileselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.ui.BindingViewHolder
import kotterknife.bindView
import java.io.File

class FileSelectionViewHolder(
  itemView: View,
  onItemClicked: OnItemClick
) : BindingViewHolder<File>(itemView) {
  private val name: TextView by bindView(R.id.file_selection__item_name)
  private val icon: ImageView by bindView(R.id.file_selection__item_type)

  init {
    itemView.setOnClickListener { onItemClicked(adapterPosition) }
  }

  override fun bindTo(item: File) {
    name.text = item.name
    val iconResId = if (item.isDirectory) {
      R.drawable.folder_drawable
    } else {
      R.drawable.file_drawable
    }
    icon.setImageResource(iconResId)
  }

  companion object {
    fun create(parent: ViewGroup, onItemClicked: OnItemClick): FileSelectionViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.item__file, parent, false)
      return FileSelectionViewHolder(view, onItemClicked)
    }
  }
}