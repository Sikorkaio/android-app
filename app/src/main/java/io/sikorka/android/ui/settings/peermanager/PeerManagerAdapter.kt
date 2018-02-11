package io.sikorka.android.ui.settings.peermanager

import android.view.ViewGroup
import io.sikorka.android.core.configuration.peers.PeerEntry
import io.sikorka.android.ui.DataAdapter

class PeerManagerAdapter : DataAdapter<PeerEntry, PeerManagerViewHolder>() {

  private val selection: MutableSet<Int> = HashSet()

  private var selectionMode: Boolean = false

  fun selectionMode(selectionMode: Boolean) {
    this.selectionMode = selectionMode
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PeerManagerViewHolder {
    return PeerManagerViewHolder.create(checkNotNull(parent)).apply {
      setOnEntrySelected { position, selected ->
        if (selected) {
          selection.remove(position)
        } else {
          selection.add(position)
        }
      }
    }
  }

  override fun onBindViewHolder(holder: PeerManagerViewHolder?, position: Int) {
    holder?.run {
      bindTo(getItem(position))
      selectionMode(selectionMode)
      setSelected(selection.contains(position))
    }
  }

  fun selectAll() {
    (0 until itemCount).forEach { position ->
      selection.add(position)
    }
    notifyDataSetChanged()
  }

  fun selectNode() {
    selection.clear()
    notifyDataSetChanged()
  }

}