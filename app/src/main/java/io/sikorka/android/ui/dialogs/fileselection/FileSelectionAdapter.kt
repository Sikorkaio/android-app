package io.sikorka.android.ui.dialogs.fileselection

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import io.sikorka.android.ui.dialogs.fileselection.Selection.DIRECTORY
import io.sikorka.android.ui.dialogs.fileselection.Selection.FILE
import java.io.File

class FileSelectionAdapter : ListAdapter<File, FileSelectionViewHolder>(DIFF_CALLBACK) {

  private lateinit var onFileSelected: OnFilePressed

  private var selectedItemPosition = -1
  @SelectionMode
  private var selectionMode: Int = FILE

  fun setSelectionMode(@SelectionMode selectionMode: Int) {
    this.selectionMode = selectionMode
  }

  fun setOnFileSelected(onFileSelected: OnFilePressed) {
    this.onFileSelected = onFileSelected
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileSelectionViewHolder {
    return FileSelectionViewHolder.create(parent) { position, long ->
      val item = getItem(position)

      val isDirectoryOnly = selectionMode == DIRECTORY
      if (isDirectoryOnly) {
        val selected = onFileSelected(item, !long)
        notifySelectionChange(if (selected) position else -1)
        return@create
      }

      onFileSelected(item, item.isDirectory)
      notifySelectionChange(position)
    }
  }

  private fun notifySelectionChange(position: Int) {
    val oldPosition = selectedItemPosition
    selectedItemPosition = position

    notifyItemChanged(position, SELECTION)
    notifyItemChanged(oldPosition, SELECTION)
  }

  override fun onBindViewHolder(holder: FileSelectionViewHolder, position: Int) {
    val item = getItem(position)
    holder.run {
      bindTo(item)
      selected(position == selectedItemPosition)
      selectionMode(selectionMode)
    }
  }

  override fun onBindViewHolder(holder: FileSelectionViewHolder, position: Int, payloads: MutableList<Any>) {
    if (payloads.contains(SELECTION)) {
      holder.selectionMode(selectionMode)
      holder.selected(position == selectedItemPosition)
      return
    }

    super.onBindViewHolder(holder, position, payloads)
  }

  companion object {
    private const val SELECTION = "selection"

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<File>() {
      override fun areItemsTheSame(oldItem: File?, newItem: File?): Boolean {
        return oldItem?.absolutePath == newItem?.absolutePath
      }

      override fun areContentsTheSame(oldItem: File?, newItem: File?): Boolean {
        return oldItem == newItem
      }
    }
  }
}