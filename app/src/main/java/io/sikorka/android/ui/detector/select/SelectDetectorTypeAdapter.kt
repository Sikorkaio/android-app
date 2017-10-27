package io.sikorka.android.ui.detector.select

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.sikorka.android.R

class SelectDetectorTypeAdapter(
    context: Context,
    private val detectors: List<SupportedDetector>
) : RecyclerView.Adapter<SelectDetectorTypeAdapter.SelectDetectorViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var onSelection: ((typeId: Int) -> Unit)? = null

  override fun getItemCount(): Int = detectors.size

  override fun onBindViewHolder(holder: SelectDetectorViewHolder?, position: Int) {
    holder?.let {
      it.update(detectors[it.adapterPosition])
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SelectDetectorViewHolder {
    val view = inflater.inflate(R.layout.item__detector_type, parent, false)
    val holder = SelectDetectorViewHolder(view)
    holder.onSelection { onSelection?.invoke(detectors[it].id) }
    return holder
  }

  fun setOnSelection(onSelection: ((typeId: Int) -> Unit)?) {
    this.onSelection = onSelection
  }

  class SelectDetectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.detector_type__type_icon)
    lateinit var icon: ImageView

    @BindView(R.id.detector_type__type_name)
    lateinit var name: TextView


    init {
      ButterKnife.bind(this, itemView)
    }

    fun update(supportedDetector: SupportedDetector) {
      icon.setImageResource(supportedDetector.icon)
      name.setText(supportedDetector.title)
    }

    fun onSelection(onSelection: (adapterPosition: Int) -> Unit) {
      this.itemView.setOnClickListener { onSelection(adapterPosition) }
    }
  }
}