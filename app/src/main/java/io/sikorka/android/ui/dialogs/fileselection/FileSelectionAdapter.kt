package io.sikorka.android.ui.dialogs.fileselection

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import java.io.File

class FileSelectionAdapter : ListAdapter<File, FileSelectionViewHolder>(DIFF_CALLBACK) {

  private lateinit var onFileSelected: OnFileSelected

  fun setOnFileSelected(onFileSelected: OnFileSelected) {
    this.onFileSelected = onFileSelected
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileSelectionViewHolder {
    return FileSelectionViewHolder.create(parent) { position ->
      onFileSelected(getItem(position))
    }
  }

  override fun onBindViewHolder(holder: FileSelectionViewHolder, position: Int) {
    holder.bindTo(getItem(position))
  }

  companion object {
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