package io.sikorka.android.ui.settings.peermanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.sikorka.android.R
import io.sikorka.android.core.configuration.peers.PeerEntry
import kotterknife.bindView

class PeerManagerViewHolder(itemView: View) : ViewHolder(itemView) {

  private val nodeId: TextView by bindView(R.id.peers__node_id)
  private val nodeAddress: TextView by bindView(R.id.peers__node_address)
  private val nodeSelected: CheckBox by bindView(R.id.peers__node_selected)

  private lateinit var onEntrySelected: (position: Int, selected: Boolean) -> Unit
  private val onCheckedListener: (CompoundButton, Boolean) -> Unit = { _, selected ->
    onEntrySelected(adapterPosition, selected)
  }

  fun bindTo(peer: PeerEntry) {
    nodeSelected.setOnCheckedChangeListener(null)
    nodeId.text = peer.nodeId
    nodeAddress.text = peer.nodeAddress
    nodeSelected.setOnCheckedChangeListener(onCheckedListener)
  }

  companion object {
    fun create(parent: ViewGroup): PeerManagerViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.item__peer, parent, false)
      return PeerManagerViewHolder(view)
    }
  }

  fun setOnEntrySelected(onEntrySelected: (position: Int, selected: Boolean) -> Unit) {
    this.onEntrySelected = onEntrySelected
  }

  fun selectionMode(selectionMode: Boolean) {
    nodeSelected.isVisible = selectionMode
  }

  fun setChecked(checked: Boolean) {
    nodeSelected.isChecked = checked
  }
}