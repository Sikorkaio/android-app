package io.sikorka.android.ui.detector.bluetooth

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.sikorka.android.R
import kotterknife.bindView

class FindDetectorAdapter : Adapter<FindDetectorViewHolder>() {

  private var data: List<BluetoothDevice> = emptyList()
  private var onClick: ((device: BluetoothDevice) -> Unit)? = null

  override fun onBindViewHolder(holder: FindDetectorViewHolder, position: Int) {
    val adapterPosition = holder.adapterPosition
    val device = data[adapterPosition]
    holder.setName(device.name)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindDetectorViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.item__detector, parent, false)
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

class FindDetectorViewHolder(itemView: View) : ViewHolder(itemView) {

  private val detectorName: TextView by bindView(R.id.find_detector__detector_name)

  fun setName(name: String) {
    detectorName.text = name
  }

  fun setOnClickListener(onClick: ((position: Int) -> Unit)) {
    itemView.setOnClickListener { onClick(adapterPosition) }
  }
}