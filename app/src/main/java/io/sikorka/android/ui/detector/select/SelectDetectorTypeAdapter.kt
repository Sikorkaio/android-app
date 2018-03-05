package io.sikorka.android.ui.detector.select

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.ui.BindingViewHolder
import kotterknife.bindView

class SelectDetectorTypeAdapter(
  context: Context,
  private val detectors: List<SupportedDetector>
) : RecyclerView.Adapter<SelectDetectorTypeAdapter.SelectDetectorViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var onSelection: ((typeId: Int) -> Unit)? = null

  override fun getItemCount(): Int = detectors.size

  override fun onBindViewHolder(holder: SelectDetectorViewHolder, position: Int) {
    holder.bindTo(detectors[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDetectorViewHolder {
    val view = inflater.inflate(R.layout.item__detector_type, parent, false)
    val holder = SelectDetectorViewHolder(view)
    holder.onSelection { onSelection?.invoke(detectors[it].id) }
    return holder
  }

  fun setOnSelection(onSelection: ((typeId: Int) -> Unit)?) {
    this.onSelection = onSelection
  }

  class SelectDetectorViewHolder(itemView: View) : BindingViewHolder<SupportedDetector>(itemView) {

    private val icon: ImageView by bindView(R.id.detector_type__type_icon)
    private val name: TextView by bindView(R.id.detector_type__type_name)

    override fun bindTo(item: SupportedDetector) {
      icon.setImageResource(item.icon)
      name.setText(item.title)
    }

    fun onSelection(onSelection: (adapterPosition: Int) -> Unit) {
      this.itemView.setOnClickListener { onSelection(adapterPosition) }
    }
  }
}