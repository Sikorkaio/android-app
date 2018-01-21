package io.sikorka.android.ui.detector.bluetooth

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.bind
import javax.inject.Inject

class FindDetectorAdapter
@Inject constructor() : RecyclerView.Adapter<FindDetectorViewHolder>() {

  private var data: List<BluetoothDevice> = emptyList()
  private var onClick: ((device: BluetoothDevice) -> Unit)? = null

  override fun onBindViewHolder(holder: FindDetectorViewHolder?, position: Int) {
    val adapterPosition = holder?.adapterPosition ?: fail("null holder")
    val device = data[adapterPosition]
    holder.setName(device.name)
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FindDetectorViewHolder {
    val parentView = parent ?: fail("parent was null")
    val inflater = LayoutInflater.from(parentView.context)
    val view = inflater.inflate(R.layout.item__detector, parentView, false)
    val holder = FindDetectorViewHolder(view)
    holder.setOnClickListener { onClick?.invoke(data[it]) }
    return holder
  }

  override fun getItemCount(): Int = data.size

  fun update(devices: List<BluetoothDevice>) {
    data = devices
    notifyDataSetChanged()
  }

  fun setOnClickListener(onClick: ((device: BluetoothDevice) -> Unit)?) {
    this.onClick = onClick
  }
}

class FindDetectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val detectorName: TextView by itemView.bind(R.id.find_detector__detector_name)

  fun setName(name: String) {
    detectorName.text = name
  }

  fun setOnClickListener(onClick: ((position: Int) -> Unit)) {
    itemView.setOnClickListener { onClick(adapterPosition) }
  }
}