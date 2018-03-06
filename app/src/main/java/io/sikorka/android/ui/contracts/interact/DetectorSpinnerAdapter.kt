package io.sikorka.android.ui.contracts.interact

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.ui.detector.select.SupportedDetector

class DetectorSpinnerAdapter(
  private val data: List<SupportedDetector>
) : BaseAdapter() {

  override fun getCount(): Int = data.size

  override fun getItem(i: Int): SupportedDetector = data[i]

  override fun getItemId(i: Int): Long = data[i].id.toLong()

  @SuppressLint("InflateParams")
  override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
    val itemView = if (view == null) {
      val inflater = LayoutInflater.from(checkNotNull(viewGroup?.context))
      inflater.inflate(R.layout.item__detector_type, null)
    } else {
      view
    }

    return itemView.run {
      val detectors = data[i]

      val icon: ImageView = findViewById(R.id.detector_type__type_icon)
      val names: TextView = findViewById(R.id.detector_type__type_name)

      icon.setImageResource(detectors.icon)
      names.setText(detectors.title)
      this
    }
  }
}