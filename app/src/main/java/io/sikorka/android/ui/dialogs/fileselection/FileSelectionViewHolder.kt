package io.sikorka.android.ui.dialogs.fileselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.view.isVisible
import io.sikorka.android.R
import io.sikorka.android.ui.BindingViewHolder
import io.sikorka.android.ui.dialogs.fileselection.Selection.FILE
import kotterknife.bindView
import java.io.File


class FileSelectionViewHolder(
  itemView: View,
  onItemClicked: OnItemClick
) : BindingViewHolder<File>(itemView) {
  private val name: TextView by bindView(R.id.file_selection__item_name)
  private val icon: ImageView by bindView(R.id.file_selection__item_type)
  private val highlight: View by bindView(R.id.file_selection__selection_highlight)

  private var isFile = false

  @SelectionMode
  private var selectionMode: Int = FILE

  init {
    itemView.setOnClickListener { onItemClicked(adapterPosition, false) }
    itemView.setOnLongClickListener {
      onItemClicked(adapterPosition, true)
      return@setOnLongClickListener true
    }
  }

  override fun bindTo(item: File) {

    name.text = item.name
    isFile = item.isFile
    val iconResId = if (isFile) {
      R.drawable.file_drawable
    } else {
      R.drawable.folder_drawable
    }
    icon.setImageResource(iconResId)
  }

  fun selected(isSelected: Boolean) {

    val canBeSelected = if (selectionMode == FILE) {
      isFile
    } else {
      !isFile
    }

    highlight.isVisible = canBeSelected && isSelected
  }

  fun selectionMode(@SelectionMode selectionMode: Int) {
    this.selectionMode = selectionMode
  }

  companion object {
    fun create(parent: ViewGroup, onItemClicked: OnItemClick): FileSelectionViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.item__file, parent, false)
      return FileSelectionViewHolder(view, onItemClicked)
    }
  }
}