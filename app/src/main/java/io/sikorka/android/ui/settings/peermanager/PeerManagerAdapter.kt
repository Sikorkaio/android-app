package io.sikorka.android.ui.settings.peermanager

import android.view.ViewGroup
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.ui.DataAdapter

class PeerManagerAdapter : DataAdapter<PeerEntry, PeerManagerViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PeerManagerViewHolder {
    return PeerManagerViewHolder.create(checkNotNull(parent))
  }

  override fun onBindViewHolder(holder: PeerManagerViewHolder?, position: Int) {
    holder?.bindTo(getItem(position))
  }

}