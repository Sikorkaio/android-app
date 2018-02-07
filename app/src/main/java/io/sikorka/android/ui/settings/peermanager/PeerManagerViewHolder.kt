package io.sikorka.android.ui.settings.peermanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.core.configuration.peers.PeerEntry
import kotterknife.bindView

class PeerManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val nodeId: TextView by bindView(R.id.peers__node_id)
  private val nodeAddress: TextView by bindView(R.id.peers__node_address)

  fun bindTo(peer: PeerEntry) {
    nodeId.text = peer.nodeId
    nodeAddress.text = peer.nodeAddress
  }

  companion object {
    fun create(parent: ViewGroup): PeerManagerViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.item__peer, parent, false)
      return PeerManagerViewHolder(view)
    }
  }
}