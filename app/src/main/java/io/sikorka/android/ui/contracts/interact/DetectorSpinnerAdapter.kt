package io.sikorka.android.ui.contracts.interact


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.ui.detector.select.SupportedDetector


class DetectorSpinnerAdapter(private var context: Context, private val data: List<SupportedDetector>) : BaseAdapter() {
  private var inflater: LayoutInflater = LayoutInflater.from(context)

  override fun getCount(): Int = data.size

  override fun getItem(i: Int): SupportedDetector = data[i]

  override fun getItemId(i: Int): Long = data[i].id.toLong()

  override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
    var thisView = view
    thisView = inflater.inflate(R.layout.item__detector_type, null)
    val icon: ImageView = thisView.findViewById(R.id.detector_type__type_icon)
    val names: TextView = thisView.findViewById(R.id.detector_type__type_name)
    val detectors = data[i]
    icon.setImageResource(detectors.icon)
    names.setText(detectors.title)
    return thisView
  }
}